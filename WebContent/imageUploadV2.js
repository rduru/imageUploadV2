var chunks = [];//切塊之後以array表示

$( document ).ready(function() {
    console.log( "ready!" );
    
    var uuid;//unique id
    var maxLength;//切分的總數
    var testStr = 'aaabbbcccdddeeefff';//假設是那個被轉換成base64之後的圖片字串
    var chunkSize = 3;//切分的大小
    
    //進行切塊
    for( var i =0 ; i<testStr.length; i ) {
        var count = chunks.length;
        var str = testStr.slice(i, i+chunkSize);
        chunks.push(count+'_'+str);
        i = i+chunkSize;
    }
    //切成幾塊就設定為最大長度
    maxLength = chunks.length;
    
    //宣告取UUID的XHR
    //先取UUID 作為之後要組起來時的識別key
    var xhrGetUUID = new XMLHttpRequest();
    xhrGetUUID.timeout = 60000; // time in milliseconds
    xhrGetUUID.open("POST", "getUUID.do");
    xhrGetUUID.addEventListener('load', function(e) {
        //成功取到之後就先傳第一塊
        uuid = this.responseText;
        var chunk = getChunk();//取塊的方法
        var param = uuid+'（）'+chunk.split('_')[0]+'（）'+maxLength+'（）'+chunk.split('_')[1]
            +'（）testPicType（）testCaseNo（）testDocPhoto';
        xhrImgUpload.send(param);
    })

    //宣告實際進行上傳的XHR
    var xhrImgUpload = new XMLHttpRequest();
    xhrImgUpload.timeout = 60000; // time in milliseconds
    xhrImgUpload.open("POST", "imageUpload.do");
    xhrImgUpload.addEventListener('load', function(e) {
        //根據server的回應做不同的處理 格式會是 index_status
        var result = this.responseText;
        
        /*
         * index 一般表示上傳的那一塊是第幾塊, 如果是full代表全部做完了
         */
        var index = result.split('_')[0];
        
        /*
         * status表示上傳結果 success成功 failed失敗
         */
        var status = result.split('_')[1];
        
        //上傳成功
        if( status == 'success' ) {
            if( index == 'full' ) {
                //full代表上傳完成, 就繼續做原本傳完圖之後要做的事
                return;
            } else {
                //還有其他塊要上傳, 先清掉已經傳完的
                //因為這邊的邏輯會是一塊上傳完才做下一塊, 所以清除的部分是使用pop, pop默認會把最後一個移除
                //為了配合pop的邏輯所以getChunk也會從最尾端開始拿
                //所以假設今天有 0 1 2 3 4 5 六塊
                //上傳的時候會是 傳5  > 5成功 > 傳4 > 4失敗 > 傳4 > 4成功 > 傳3 ...
                removeChunk();
            }
        }
        var chunk = getChunk();//拿出下一塊要上傳的
        if(chunk == 'clean' ) {
            return;
        }
        var param = uuid+'（）'+chunk.split('_')[0]+'（）'+maxLength+'（）'+chunk.split('_')[1]
        +'（）testPicType（）testCaseNo（）testDocPhoto';
        xhrImgUpload.open("POST", "imageUpload.do");
        xhrImgUpload.send(param);
    })
    
    //起點 整個流程會從取UUID開始
    //取UUID > 成功取到 > 傳第一塊 > 第一塊成功 > 傳第二塊 > ...
    xhrGetUUID.send();
});

function getChunk() {
    if( chunks.length <= 0) {
        return 'clean';
    } else {
        return chunks[chunks.length-1];
    }
}

function removeChunk() {
    chunks.pop();
}