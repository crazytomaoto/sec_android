package com.hysd.android.platform_huanuo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.util.Log;

/**
 * FileName    : Logger.java
 * Description : 日志输出
 * @Copyright  : hysdpower. All Rights Reserved
 * @Company    :  
 * @author     : 刘剑
 * @version    : 1.0
 * Create Date : 2014-4-8 下午2:01:52
 **/
public final class Logger {

	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int NONE = 99;
	private static int currentLevel = 2;

	private static boolean isStoreLogFile = false;
	private static final int FILE_AMOUNT = 5;
	private static final long MAXSIZE_PERFILE = 1048576L;

	public static void initLogger(int logLevel, boolean isStore, String storePath) {
		currentLevel = logLevel;
		isStoreLogFile = isStore;

		if (isStore) {
			if (storePath != null) {
				LogCache.getInstance().initLogWriter(storePath, 5, 1048576L);
			} else {
				isStoreLogFile = false;
			}
		}

		if ((!isStoreLogFile) && (LogCache.getInstance().isStarted())) {
			LogCache.getInstance().stop();
		}
	}

	private static int println(int level, String tag, String msg) {
		int result = 0;

		if (isLoggable(level)) {
			result = Log.println(level, tag, msg);
		} else {
			return result;
		}

		if (!isStoreLogFile) {
			return result;
		}

		if (!LogCache.getInstance().isStarted()) {
			startService();
		}

		LogCache.getInstance().write(levelString(level), tag, msg);

		return result;
	}

	private static String levelString(int level) {
		switch (level) {
			case 2:
				return "V";
			case 3:
				return "D";
			case 4:
				return "I";
			case 5:
				return "W";
			case 6:
				return "E";
		}
		return "D";
	}

	public static int v(String tag, String msg) {
		return println(VERBOSE, tag, msg);
	}

	public static int v(String tag, String msg, Throwable tr) {
		return println(VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
	}

	public static int d(String tag, String msg) {
		return println(DEBUG, tag, msg);
	}

	public static int d(String tag, String msg, Throwable tr) {
		return println(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
	}

	public static int i(String tag, String msg) {
		return println(INFO, tag, msg);
	}

	public static int i(String tag, String msg, Throwable tr) {
		return println(INFO, tag, msg + '\n' + getStackTraceString(tr));
	}

	public static int w(String tag, String msg) {
		return println(WARN, tag, msg);
	}

	public static int w(String tag, String msg, Throwable tr) {
		return println(WARN, tag, msg + '\n' + getStackTraceString(tr));
	}

	public static int w(String tag, Throwable tr) {
		return println(WARN, tag, getStackTraceString(tr));
	}

	public static int e(String tag, String msg) {
		return println(ERROR, tag, msg);
	}

	public static int e(String tag, Throwable tr) {
		return println(ERROR, tag, getStackTraceString(tr));
	}

	public static int e(String tag, String msg, Throwable tr) {
		return println(ERROR, tag, msg + '\n' + getStackTraceString(tr));
	}

	private static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}

	private static boolean isLoggable(int level) {
		return level >= currentLevel;
	}

	private static synchronized void startService() {
		LogCache.getInstance().start();
	}

	private static class LogWriter {
		public static final String TAG = "Logger";
		private final Comparator<File> c = new Comparator<File>() {
			public int compare(File f1, File f2) {
				return String.CASE_INSENSITIVE_ORDER.compare(f1.getName(), f2.getName());
			}
		};
		private File current;
		private int fileAmount = 2;

		private long maxSize = 1048576L;

		private ArrayList<File> historyLogs = null;

		private SimpleDateFormat timestampOfName = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		private PrintWriter writer = null;
		private static LogWriter INSTANCE;

		private LogWriter() {
		}

		public static LogWriter getInstance(File current, int fileAmount, long maxSize) {
			if (INSTANCE == null) {
				INSTANCE = new LogWriter(current, fileAmount, maxSize);
			}

			return INSTANCE;
		}

		private LogWriter(File current, int fileAmount, long maxSize) {
			this.current = current;
			this.fileAmount = (fileAmount <= 0 ? this.fileAmount : fileAmount);
			this.maxSize = (maxSize <= 0L ? this.maxSize : maxSize);
			initialize();
		}

		public synchronized boolean initialize() {
			Log.v("Logger", "initializing... ");
			try {
				if (this.current == null) {
					return false;
				}

				if (!this.current.getParentFile().exists()) {
					if (!this.current.getParentFile().mkdirs()) {
						return false;
					}
				} else if (null == this.historyLogs) {
					File[] fs = this.current.getParentFile().listFiles(new FilenameFilter() {
						public boolean accept(File dir, String filename) {
							String curName = Logger.LogWriter.this.current.getName();
							String patt = curName.replace(curName.substring(curName.lastIndexOf(".")), "_");

							return filename.contains(patt);
						}
					});
					if ((fs != null) && (fs.length != 0)) {
						this.historyLogs = new ArrayList(Arrays.asList(fs));
					} else {
						this.historyLogs = new ArrayList();
					}
				}
				this.writer = new PrintWriter(new FileOutputStream(this.current, (this.current.exists()) && (isCurrentAvailable())), true);

				return true;
			} catch (FileNotFoundException e) {
				Log.e("LogWriter", "", e);
			}
			return false;
		}

		private File getTheEarliest() {
			Collections.sort(this.historyLogs, this.c);
			return (File) this.historyLogs.get(0);
		}

		public boolean rotate() {
			File des = new File(newName());
			if (this.historyLogs.size() >= this.fileAmount - 1) {
				boolean deleteResult = forceDeleteFile(getTheEarliest());
				if (deleteResult) {
					Log.i("Logger", "old historyLogs: " + this.historyLogs);
					Log.i("Logger", "delete " + ((File) this.historyLogs.get(0)).getName() + "successfully.");

					this.historyLogs.remove(0);
				} else {
					Log.i("Logger", "delete " + ((File) this.historyLogs.get(0)).getName() + "abortively.");

					return false;
				}
			}

			try {
				close();
				boolean result = this.current.renameTo(des);
				if ((!result) || (!initialize())) {
					Log.v("Logger", "rename or initialize error!");
					return false;
				}
			} catch (Exception e) {
				Log.e("Logger", "", e);
				return false;
			}
			this.historyLogs.add(des);
			Log.i("Logger", "new historyLogs: " + this.historyLogs);

			return true;
		}

		public boolean isCurrentExist() {
			return this.current.exists();
		}

		public boolean isCurrentAvailable() {
			return this.current.length() < this.maxSize;
		}

		public String newName() {
			String name = this.current.getAbsolutePath();
			int dox = name.lastIndexOf('.');
			String prefix = name.substring(0, dox) + "_";
			String suffix = name.substring(dox);
			return prefix + this.timestampOfName.format(Long.valueOf(System.currentTimeMillis())) + suffix;
		}

		private boolean deleteTheEarliest() {
			return this.historyLogs.size() == 0 ? false : getTheEarliest().delete();
		}

		public void println(String msg) {
			if (null == this.writer) {
				initialize();
			} else {
				this.writer.println(msg);
			}
		}

		public boolean clearSpace() {
			return deleteTheEarliest();
		}

		public synchronized void close() {
			if (null != this.writer) {
				this.writer.close();
			}
		}

		private static boolean forceDeleteFile(File file) {
			boolean result = false;
			int tryCount = 0;
			while ((!result) && (tryCount++ < 10)) {
				result = file.delete();
				if (result)
					continue;
				try {
					synchronized (file) {
						file.wait(200L);
					}
				} catch (InterruptedException e) {
					Logger.e("Logger", "forceDeleteFile:", e);
				}

			}

			return result;
		}
	}

	private static class MemoryStatus {
		private static final int ERROR = -1;

		public static long getAvailableExternalMemorySize() {
			if (Environment.getExternalStorageState().equals("mounted")) {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return availableBlocks * blockSize;
			}

			return -1L;
		}

		public static boolean isExternalMemoryAvailable(long size) {
			long availableMemory = getAvailableExternalMemorySize();
			return size <= availableMemory;
		}
	}

	private static class LogCache {
		public static final String TAG = "Logger";
		private static LogCache INSTANCE = null;

		private static final GregorianCalendar CALENDAR_INSTANCE = new GregorianCalendar();

		private final BlockingQueue<String> queue = new LinkedBlockingQueue();
		private volatile boolean started;
		private volatile Thread logWorkerThread;
		private Logger.LogWriter logWriter = null;

		private int counter = 0;

		public static LogCache getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new LogCache();
			}

			return INSTANCE;
		}

		public void initLogWriter(String filePath, int fileAmount, long maxSize) {
			this.logWriter = Logger.LogWriter.getInstance(new File(filePath), fileAmount, maxSize);
		}

		public void write(String msg) {
			if (this.started) {
				try {
					this.queue.put(msg);
				} catch (InterruptedException e) {
					Log.w("LogCache", msg, e);
				}
			}
		}

		public void write(String level, String tag, String msg) {
			CALENDAR_INSTANCE.setTimeInMillis(System.currentTimeMillis());
			int pid = Process.myPid();
			int month = CALENDAR_INSTANCE.get(2) + 1;
			int date = CALENDAR_INSTANCE.get(5);
			int hour = CALENDAR_INSTANCE.get(11);
			int minute = CALENDAR_INSTANCE.get(12);
			int seconds = CALENDAR_INSTANCE.get(13);

			StringBuilder sbr = new StringBuilder();
			sbr.append(month).append('-').append(date).append(' ');
			sbr.append(hour).append(':').append(minute).append(':').append(seconds);
			sbr.append('\t').append(level).append('\t').append(pid);
			sbr.append('\t').append('[').append(Thread.currentThread().getName()).append(']');

			sbr.append('\t').append(tag).append('\t').append(msg);
			write(sbr.toString());
		}

		public boolean isStarted() {
			return this.started;
		}

		public synchronized void start() {
			if (null == this.logWorkerThread) {
				this.logWorkerThread = new Thread(new LogTask(), "Log Worker Thread - " + this.counter);
			}

			if ((this.started) || (!this.logWriter.initialize())) {
				return;
			}

			this.started = true;
			this.logWorkerThread.start();
		}

		public synchronized void stop() {
			if (!this.started) {
				return;
			}

			this.started = false;
			this.queue.clear();
			this.logWriter.close();
			if (null != this.logWorkerThread) {
				this.logWorkerThread.interrupt();
				this.logWorkerThread = null;
			}
		}

		private final class LogTask implements Runnable {
			public LogTask() {
			}

			private void dealMsg() throws InterruptedException {
				String msg = null;
				while ((Logger.LogCache.this.started) && (!Thread.currentThread().isInterrupted())) {
					msg = (String) Logger.LogCache.this.queue.take();
					try {
						synchronized (Logger.LogCache.this.logWriter) {
							if (Logger.MemoryStatus.isExternalMemoryAvailable(msg.getBytes().length)) {
								if (!Logger.LogCache.this.logWriter.isCurrentExist() ? !Logger.LogCache.this.logWriter.initialize() : (!Logger.LogCache.this.logWriter
										.isCurrentAvailable()) && (!Logger.LogCache.this.logWriter.rotate())) {
									continue;
								}

								Logger.LogCache.this.logWriter.println(msg);
							} else if (Logger.LogCache.this.logWriter.clearSpace()) {
								if (!Logger.LogCache.this.logWriter.rotate()) {
									continue;
								}
								Logger.LogCache.this.logWriter.println(msg);
							} else {
								Log.e("Logger", "can't log into sdcard.");
							}
						}
					} catch (Throwable e) {
						if ((e instanceof InterruptedException)) {
							throw ((InterruptedException) e);
						}
						if ((e instanceof RuntimeException))
							throw ((RuntimeException) e);
					}
				}
			}

			public void run() {
				try {
					dealMsg();
				} catch (InterruptedException e) {
					Log.e("Logger", Thread.currentThread().toString(), e);
				} catch (RuntimeException e) {
					Log.e("Logger", Thread.currentThread().toString(), e);
				} finally {
				}
			}
		}
	}
}
