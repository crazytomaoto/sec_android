package com.hualianzb.sec.models;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/10/10
 * auther:wangtianyun
 * describe:
 */
public class RecordTimeBean implements Serializable {

    /**
     * jsonrpc : 2.0
     * id : 1
     * result : {"difficulty":"0x2","extraData":"0xd88301080e846765746888676f312e31302e33856c696e757800000000000000ed5e64d0e9a3e5bb7687a28920956b1f332b3a6b18fab742c74deecb108b84f1312ad3814c8d263c8b9d24a7230a7d4741e05e074fcc8757e3688535b278a52101","gasLimit":"0x765571","gasUsed":"0x58f06b","hash":"0x983a1f24ee2bfab6c1ebbae9a0cc86054a90dc6f413a3442d2aa29b50616cb85","logsBloom":"0x00100000000401000100000000000010100000000002000000000000000000000010000000002000100000000000000010000000c00010400020000000260002000080000004000040000008004000000000000000040000000000000010000000000000001040000000000000800000000000000000000000000210000000000000020000080001000000000200000000800000000000018000000000420000020020000000020000000000000100020004900004000000001400040000000100010002000008000000000000014000001000000000000000100020000040200010000000000100000000020000000000000000200000000800080010200220","miner":"0x0000000000000000000000000000000000000000","mixHash":"0x0000000000000000000000000000000000000000000000000000000000000000","nonce":"0x0000000000000000","number":"0x2e868d","parentHash":"0xae985b61c43cdea292d3e3677d939f0ae0976c5b4acb228ca398c78a3fe20598","receiptsRoot":"0x3e5ff92a7646cbdf6a6b48edee7a0f2dbad7be56c3d8d367ca216548266090da","sha3Uncles":"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347","size":"0x3372","stateRoot":"0x2bd72a48af3e2801fc8a337d2fe733be1d8bcfae08f9371f91eebd3599657c29","timestamp":"0x5ba9ee8a","totalDifficulty":"0x569938","transactions":["0xdfd7f7428a50ee58d4bd8c36f2199ab44ae0a5be5dec486c3fbe70bdfdaef844","0xdc71bffb31773290b2f7a732277903eb1b2baa22c5a810aedddd07e79ee87911","0x0ba7f16b23c640fbaf6b9e796cf6800d10e8144df3590fd1fb62ff4b7645dd9a","0x387d313bdade3e51b7caec0b6f67a2df16aa7c7ac2187f0daf0c4fd9906bc30b","0x387f6fe6da10253e9a7005af70d98394eb960451d1a6f4ff0da0270d7e67288e","0x723028cdb85d138945707ca451378dfc5cbb4f21801f0a14dd6203a00a795597","0x4b1a9ad6c320717ac6b59b2a64ce7f7fef0cd60ac4fbe7bc6fb2b2c19d4774cb","0x1e7b0dd3d5430b3217b945594d64b4e779d44733b729117ed598e4aeee0ff3df","0x7cb11bbccf845e4f618ea129b45a63d62ed026765832dc17220408e3c00ceb0a","0x9617238acfdf968502ac0c560f6fe071f3b64615e742ce6e22be7fc7207eec61","0xbd20314a6aa013b5637581ad6807a5b117949ddd5c3156161b413c4828bca001","0x0227c2d9086d3c2fe0ac7f7b0e4e776a5fa2bbc449b52bfab532841d15481bb7","0xd3759ebb38a4ce231e229b4bfd6f6db41b016f7545980eb374e9f984af24bf7f","0xdfa132beb14fbf5c9addbdc261eba7e38de6134067b69a9b2b32da4deae9167f","0x18cc791fa0e0e577ec6beedc038aa6af788102a04d564a9cfbb405b0732420dc","0xd9aeab776807bd21358de4308c8b57b4b4e1430f30f0d448043e13dd291096c7","0x6d13f3bfa0e4df58487c0ca5ff3a318fbdc8a2e875eeab2af81ec281e4d0b5bd","0xe0db571f5be00eae9ff91b87405fff978e1e05ff623b2e7aeefa4bb892622b11","0xb0cf706cd3c4ab0148d89bbb5dbac924f99c4b8f31d255d9f08bc147aa97be32","0x5aa838e1239b43d32cec641e086b214d3e2f92f8ad32feab4ccba84aee2dfdd4"],"transactionsRoot":"0x73194ba97d50dbcc60392049608146f0751bf51800c3ed7b956458d8ef161c98","uncles":[]}
     */

    private String jsonrpc;
    private int id;
    private ResultBean result;

    public RecordTimeBean() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * difficulty : 0x2
         * extraData : 0xd88301080e846765746888676f312e31302e33856c696e757800000000000000ed5e64d0e9a3e5bb7687a28920956b1f332b3a6b18fab742c74deecb108b84f1312ad3814c8d263c8b9d24a7230a7d4741e05e074fcc8757e3688535b278a52101
         * gasLimit : 0x765571
         * gasUsed : 0x58f06b
         * hash : 0x983a1f24ee2bfab6c1ebbae9a0cc86054a90dc6f413a3442d2aa29b50616cb85
         * logsBloom : 0x00100000000401000100000000000010100000000002000000000000000000000010000000002000100000000000000010000000c00010400020000000260002000080000004000040000008004000000000000000040000000000000010000000000000001040000000000000800000000000000000000000000210000000000000020000080001000000000200000000800000000000018000000000420000020020000000020000000000000100020004900004000000001400040000000100010002000008000000000000014000001000000000000000100020000040200010000000000100000000020000000000000000200000000800080010200220
         * miner : 0x0000000000000000000000000000000000000000
         * mixHash : 0x0000000000000000000000000000000000000000000000000000000000000000
         * nonce : 0x0000000000000000
         * number : 0x2e868d
         * parentHash : 0xae985b61c43cdea292d3e3677d939f0ae0976c5b4acb228ca398c78a3fe20598
         * receiptsRoot : 0x3e5ff92a7646cbdf6a6b48edee7a0f2dbad7be56c3d8d367ca216548266090da
         * sha3Uncles : 0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347
         * size : 0x3372
         * stateRoot : 0x2bd72a48af3e2801fc8a337d2fe733be1d8bcfae08f9371f91eebd3599657c29
         * timestamp : 0x5ba9ee8a
         * totalDifficulty : 0x569938
         * transactions : ["0xdfd7f7428a50ee58d4bd8c36f2199ab44ae0a5be5dec486c3fbe70bdfdaef844","0xdc71bffb31773290b2f7a732277903eb1b2baa22c5a810aedddd07e79ee87911","0x0ba7f16b23c640fbaf6b9e796cf6800d10e8144df3590fd1fb62ff4b7645dd9a","0x387d313bdade3e51b7caec0b6f67a2df16aa7c7ac2187f0daf0c4fd9906bc30b","0x387f6fe6da10253e9a7005af70d98394eb960451d1a6f4ff0da0270d7e67288e","0x723028cdb85d138945707ca451378dfc5cbb4f21801f0a14dd6203a00a795597","0x4b1a9ad6c320717ac6b59b2a64ce7f7fef0cd60ac4fbe7bc6fb2b2c19d4774cb","0x1e7b0dd3d5430b3217b945594d64b4e779d44733b729117ed598e4aeee0ff3df","0x7cb11bbccf845e4f618ea129b45a63d62ed026765832dc17220408e3c00ceb0a","0x9617238acfdf968502ac0c560f6fe071f3b64615e742ce6e22be7fc7207eec61","0xbd20314a6aa013b5637581ad6807a5b117949ddd5c3156161b413c4828bca001","0x0227c2d9086d3c2fe0ac7f7b0e4e776a5fa2bbc449b52bfab532841d15481bb7","0xd3759ebb38a4ce231e229b4bfd6f6db41b016f7545980eb374e9f984af24bf7f","0xdfa132beb14fbf5c9addbdc261eba7e38de6134067b69a9b2b32da4deae9167f","0x18cc791fa0e0e577ec6beedc038aa6af788102a04d564a9cfbb405b0732420dc","0xd9aeab776807bd21358de4308c8b57b4b4e1430f30f0d448043e13dd291096c7","0x6d13f3bfa0e4df58487c0ca5ff3a318fbdc8a2e875eeab2af81ec281e4d0b5bd","0xe0db571f5be00eae9ff91b87405fff978e1e05ff623b2e7aeefa4bb892622b11","0xb0cf706cd3c4ab0148d89bbb5dbac924f99c4b8f31d255d9f08bc147aa97be32","0x5aa838e1239b43d32cec641e086b214d3e2f92f8ad32feab4ccba84aee2dfdd4"]
         * transactionsRoot : 0x73194ba97d50dbcc60392049608146f0751bf51800c3ed7b956458d8ef161c98
         * uncles : []
         */

        private String difficulty;
        private String extraData;
        private String gasLimit;
        private String gasUsed;
        private String hash;
        private String logsBloom;
        private String miner;
        private String mixHash;
        private String nonce;
        private String number;
        private String parentHash;
        private String receiptsRoot;
        private String sha3Uncles;
        private String size;
        private String stateRoot;
        private String timestamp;
        private String totalDifficulty;
        private String transactionsRoot;
        private List<String> transactions;
        private List<?> uncles;

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        public String getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(String gasLimit) {
            this.gasLimit = gasLimit;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getLogsBloom() {
            return logsBloom;
        }

        public void setLogsBloom(String logsBloom) {
            this.logsBloom = logsBloom;
        }

        public String getMiner() {
            return miner;
        }

        public void setMiner(String miner) {
            this.miner = miner;
        }

        public String getMixHash() {
            return mixHash;
        }

        public void setMixHash(String mixHash) {
            this.mixHash = mixHash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getParentHash() {
            return parentHash;
        }

        public void setParentHash(String parentHash) {
            this.parentHash = parentHash;
        }

        public String getReceiptsRoot() {
            return receiptsRoot;
        }

        public void setReceiptsRoot(String receiptsRoot) {
            this.receiptsRoot = receiptsRoot;
        }

        public String getSha3Uncles() {
            return sha3Uncles;
        }

        public void setSha3Uncles(String sha3Uncles) {
            this.sha3Uncles = sha3Uncles;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getStateRoot() {
            return stateRoot;
        }

        public void setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTotalDifficulty() {
            return totalDifficulty;
        }

        public void setTotalDifficulty(String totalDifficulty) {
            this.totalDifficulty = totalDifficulty;
        }

        public String getTransactionsRoot() {
            return transactionsRoot;
        }

        public void setTransactionsRoot(String transactionsRoot) {
            this.transactionsRoot = transactionsRoot;
        }

        public List<String> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<String> transactions) {
            this.transactions = transactions;
        }

        public List<?> getUncles() {
            return uncles;
        }

        public void setUncles(List<?> uncles) {
            this.uncles = uncles;
        }
    }
}
