package com.hualianzb.sec.secUtil;

import org.web3j.abi.datatypes.Array;

import java.nio.Buffer;

/**
 * Date:2018/12/21
 * auther:wangtianyun
 * describe:
 */
public class SECRlpEncode {
    public SECRlpEncode() {
    }


    /**
     * encode
     *
     * @param {Buffer, String, Integer, Array} input - Input data for RLP encode
     * @param {Buffer} buffer - Input buffer which is in RLP encoded format
     * @param {Buffer} offset - Buffer offset position
     * @return {Buffer} RLP encoded input data
     * @desc Returns input in RLP encoded format
     */

    private Object encode(Object input) {
        Object buf;
        buf = null;
        return buf;
    }


//    encode(input, buffer, offset =0) {
//
//        let buf
//
//        if (Array.isArray(input)) {
//
//            buf = Buffer.concat(input.map((item) = > this.encode(item)))
//
//            buf = Buffer.concat([this._encodeLength(buf.length, 0xc0), buf ])
//
//        } else {
//
//            buf = this._toBuffer(input)
//
//            if (!(buf.length == = 1 && buf[0] < 0x80)) {
//
//                buf = Buffer.concat([this._encodeLength(buf.length, 0x80), buf ])
//
//            }
//
//        }
//
//
//        if (buffer != = undefined) {
//
//            if (offset + buf.length > buffer.length) throw new Error('Not enough buffer size')
//
//            buf.copy(buffer, offset)
//
//            buf = buffer
//
//        }
//
//
//        this.encode.bytes = buf.length
//
//        return buf
//
//    }
}
