/*
 *  创建弹出div窗口。
 1、接口说明：DivWindow(id,title,width,height,content)  构造函数，创建一个弹出窗口对象
 参数：id 弹出窗口id；
 title：弹出窗口标题名称；
 width：弹出窗口宽度
 height：弹出窗口高度
 content： 弹出窗口显示内容

 2、接口说明： closeDivWindow(id)  关闭窗口
 参数： id 弹出窗口id

 3、接口说明：setPopupTopTitleFontColor(PopupTopTitleFontColor) 设置弹出窗口标题字体颜色
 参数：

 4、接口说明：setPopupTopBgColor(tBgColor) 设置弹出窗口标题背景颜色

 5、接口说明：setPopupColor(borderColor,bgColor,tFontColor,cBgColor,fColor) 设置弹出窗口风格,包括标题栏的背景，弹出窗口边框颜色，内容窗体背景颜色，内容窗体字体颜色

 6、接口说明：open()

 使用方法： 
 var a = new DivWindow("1","窗口测试",580,400,"Welcome to visited my personal website:<br><a href=http://www.youthfly.net target=_blank>http://www.youthfly.net</a><br><ahref=http://www ... youthfly.net</a><br><br>thx!!!=)..."L);
 a.setPopupTopBgColor("black","blue","white","white","black");
 a.open();

 生成的html：
 <div id='"window"+id'></div>  控制背景的div，使背景逐渐变暗
 <div id='"windowTopBg"+id'>
 <div id='"windowTop"+id'>
 <span id='"windowTopTitle"+id'>title</span>
 <span id='"windowTopOperate"+id'>maxORmin</span>
 <span id='"windowTopClose"+id'>close</span>
 </div>
 <div id='"windowContent"+id'>content</div>
 </div>

 @author ERIC
 @date  2007.11.8
 */

var isIe = (document.all) ? true : false;
var moveable = false;
var topDivBorderColor = "#336699";// 提示窗口的边框颜色
var topDivBgColor = "#429DE3";// 提示窗口的标题的背景颜色
var contentBgColor = "white";// 内容显示窗口的背景颜色
var contentFontColor = "black";// 内容显示窗口字体颜色
var titleFontColor = "white"; // 弹出窗口标题字体颜色
var index = 10000;// z-index;
// 创建弹出窗口，构造函数
function DivWindow(id, title, w, h, content) {
    this.id = id;// 窗口id
    this.zIndex = index + 2;
    this.title = title;// 弹出窗口名称
    this.message = content;// 弹出窗口内容
    this.width = w;// 弹出窗口宽度
    this.height = h;// 弹出窗口高度
    this.left = (document.body.clientWidth) ? (document.body.clientWidth - this.width) / 2 : 0;// 弹出窗口位置，距屏幕左边的位置
    this.top = (document.body.clientHeight) ? (document.body.clientHeight - this.height) / 2 : 0;// 弹出窗口位置，距屏幕上边的位置
    // this.init = init;
    // this.init();
    this.showTop = true;
}
// 根据构造函数设定初始值，创建弹出窗口
DivWindow.prototype = {
    // 设置弹出窗口标题字体颜色
    setPopupTopTitleFontColor : function(tFontColor) {
        titleFontColor = tFontColor;
    },
    // 设置弹出窗口标题背景颜色
    setPopupTopBgColor : function(tBgColor) {
        topDivBgColor = tBgColor;
    },
    // 设置弹出窗口风格,包括标题栏的背景，弹出窗口边框颜色，内容窗体背景颜色，内容窗体字体颜色
    setPopupColor : function(borderColor, bgColor, tFontColor, cBgColor, fColor) {
        topDivBorderColor = borderColor;
        topDivBgColor = bgColor;
        titleFontColor = tFontColor;
        contentBgColor = cBgColor;
        contentFontColor = fColor;
    },
    // 打开一个弹出窗口
    open : function() {
        var s = document.getElementsByTagName("select"); // --------------把所有select标签捉住
        for ( var j = 0; j < s.length; j++) {
            s[j].style.display = "none";
        } // --------------设为不显示，再进行下面操作

        var sWidth, sHeight;
        sWidth = document.body.clientWidth;
        sHeight = document.body.clientHeight;
        var bgObj = document.createElement("div");
        bgObj.setAttribute('id', 'window' + this.id);
        var styleStr = "top:0px;left:0px;position:absolute;background:#245;width:" + sWidth + "px;height:" + sHeight
                + "px;";
        styleStr += (isIe) ? "filter:alpha(opacity=0);" : "opacity:0;";
        bgObj.style.cssText = styleStr;
        document.body.appendChild(bgObj);
        // 让背景逐渐变暗
        showBackground(bgObj, 25);

        // 弹出窗口框体背景容器
        var windowTopBgDiv = document.createElement("div");
        windowTopBgDiv.setAttribute('id', 'windowTopBg' + this.id);
        windowTopBgDiv.style.position = "absolute";
        windowTopBgDiv.style.zIndex = this.zIndex;
        windowTopBgDiv.style.width = this.width;
        windowTopBgDiv.style.height = this.height;
        windowTopBgDiv.style.left = this.left;
        windowTopBgDiv.style.top = this.top;
        windowTopBgDiv.style.background = contentBgColor;
        windowTopBgDiv.style.fontSize = "9pt";
        windowTopBgDiv.style.cursor = "default";
        windowTopBgDiv.style.border = "1px solid " + topDivBorderColor;
        windowTopBgDiv.attachEvent("onmousedown", function() {
            if (windowTopBgDiv.style.zIndex != index) {
                index = index + 2;
                var idx = index;
                windowTopBgDiv.style.zIndex = idx;
            }
        });
        // 弹出窗口头部框体
        var windowTopDiv = document.createElement("div");
        windowTopDiv.setAttribute('id', 'windowTop' + this.id);
        windowTopDiv.style.position = "absolute";
        windowTopDiv.style.background = topDivBgColor;// "white";
        windowTopDiv.style.color = titleFontColor;
        windowTopDiv.style.cursor = "move";
        windowTopDiv.style.height = 20;
        windowTopDiv.style.width = this.width - 2 * 2;
        // 开始拖动;
        windowTopDiv.attachEvent("onmousedown", function() {
            if (event.button == 1) {
                // 锁定标题栏;
                windowTopDiv.setCapture();
                // 定义对象;
                var win = windowTopDiv.parentNode;
                // 记录鼠标和层位置;
                x0 = event.clientX;
                y0 = event.clientY;
                x1 = parseInt(win.style.left);
                y1 = parseInt(win.style.top);
                // 记录颜色;
                // topDivBgColor = windowTopDiv.style.backgroundColor;
                // 改变风格;
                // windowTopDiv.style.backgroundColor = topDivBorderColor;
                win.style.borderColor = topDivBorderColor;
                moveable = true;
            }
        });
        // 停止拖动
        windowTopDiv.attachEvent("onmouseup", function() {
            if (moveable) {
                var win = windowTopDiv.parentNode;
                win.style.borderColor = topDivBgColor;
                windowTopDiv.style.backgroundColor = topDivBgColor;
                windowTopDiv.releaseCapture();
                moveable = false;
            }
        });
        // 开始拖动
        windowTopDiv.attachEvent("onmousemove", function() {
            if (moveable) {
                var win = windowTopDiv.parentNode;
                win.style.left = x1 + event.clientX - x0;
                win.style.top = y1 + event.clientY - y0;
            }
        });
        // 双击弹出窗口
        windowTopDiv.attachEvent("ondblclick", function() {
            maxOrMinPopupDiv(windowTopOperateSpan, windowContentDiv);
        });

        // 增加一个弹出窗口标题的显示
        var windowTopTitleSpan = document.createElement("span");
        windowTopTitleSpan.setAttribute('id', 'windowTopTitle' + this.id);
        windowTopTitleSpan.style.width = this.width - 2 * 12 - 4;
        windowTopTitleSpan.style.paddingLeft = "3px";
        windowTopTitleSpan.innerHTML = this.title;

        // 增加一个弹出窗口最小化，最大化的操作
        var windowTopOperateSpan = document.createElement("span");
        windowTopOperateSpan.setAttribute('id', 'windowTopOperate' + this.id);
        windowTopOperateSpan.style.width = 12;
        windowTopOperateSpan.style.borderWidth = "0px";
        windowTopOperateSpan.style.color = titleFontColor;// "white";
        windowTopOperateSpan.style.fontFamily = "webdings";
        windowTopOperateSpan.style.cursor = "default";
        windowTopOperateSpan.innerHTML = "0";
        // 最大化或者最小化弹出窗口操作
        windowTopOperateSpan.attachEvent("onclick", function() {
            maxOrMinPopupDiv(windowTopOperateSpan, windowContentDiv);
        });

        // 增加一个弹出窗口关闭的操作
        var windowTopCloseSpan = document.createElement("span");
        windowTopCloseSpan.setAttribute('id', 'windowTopClose' + this.id);
        windowTopCloseSpan.style.width = 12;
        windowTopCloseSpan.style.borderWidth = "0px";
        windowTopCloseSpan.style.color = titleFontColor;// "white";
        windowTopCloseSpan.style.fontFamily = "webdings";
        windowTopCloseSpan.style.cursor = "default";
        windowTopCloseSpan.innerHTML = "r";
        // 关闭窗口
        windowTopCloseSpan.attachEvent("onclick", function() {
            if (this.showTop) {
                windowTopDiv.removeChild(windowTopTitleSpan);
                windowTopDiv.removeChild(windowTopOperateSpan);
                windowTopDiv.removeChild(windowTopCloseSpan);
                windowTopBgDiv.removeChild(windowTopDiv);
            }
            windowTopBgDiv.removeChild(windowContentDiv);
            document.body.removeChild(windowTopBgDiv);
            document.body.removeChild(bgObj);
            for ( var j = 0; j < s.length; j++) {
                s[j].style.display = "";
            } // --------------再给select显出来
            });

        // 内容
        var windowContentDiv = document.createElement("div");
        windowContentDiv.setAttribute('id', 'windowContent' + this.id);
        windowContentDiv.style.background = contentBgColor;
        windowContentDiv.style.color = contentFontColor;
        windowContentDiv.style.cursor = "default";
        windowContentDiv.style.height = (this.height - 20 - 4);
        windowContentDiv.style.width = "100%";
        windowContentDiv.style.position = "relative";
        windowContentDiv.style.left = 0;
        windowContentDiv.style.top = this.showTop ? 24 : 4;

        windowContentDiv.style.lineHeight = "20px";
        windowContentDiv.style.fontSize = "10pt";
        windowContentDiv.style.wordBreak = "break-all";
        windowContentDiv.style.padding = "3px";
        windowContentDiv.innerHTML = this.message;

        // 将内容写入到文件中
        if (this.showTop) {
            windowTopDiv.appendChild(windowTopTitleSpan);
            windowTopDiv.appendChild(windowTopOperateSpan);
            windowTopDiv.appendChild(windowTopCloseSpan);
            windowTopBgDiv.appendChild(windowTopDiv);
        }
        windowTopBgDiv.appendChild(windowContentDiv);
        document.body.appendChild(windowTopBgDiv);
    }
}

// 最大或者最小化探出窗口
function maxOrMinPopupDiv(windowTopOperateSpan, windowContentDiv) {
    var win = windowTopOperateSpan.parentNode.parentNode;
    var tit = windowTopOperateSpan.parentNode;
    var flg = windowContentDiv.style.display == "none";
    if (flg) {
        win.style.height = parseInt(windowContentDiv.style.height) + parseInt(tit.style.height) + 2 * 2;
        windowContentDiv.style.display = "block";
        windowTopOperateSpan.innerHTML = "0";
    } else {
        win.style.height = parseInt(tit.style.height) + 2 * 2;
        windowTopOperateSpan.innerHTML = "2";
        windowContentDiv.style.display = "none";
    }
}
// 让背景渐渐变暗
function showBackground(obj, endInt) {
    if (isIe) {
        obj.filters.alpha.opacity += 1;
        if (obj.filters.alpha.opacity < endInt) {
            setTimeout(function() {
                this.showBackground(obj, endInt)
            }, 5);
        }
    } else {
        var al = parseFloat(obj.style.opacity);
        al += 0.01;
        obj.style.opacity = al;
        if (al < (endInt / 100)) {
            setTimeout(function() {
                this.showBackground(obj, endInt)
            }, 5);
        }
    }
}
// 关闭弹出窗口
function closeDivWindow(id) {

    var windowTopTitleSpan = document.getElementById("windowTopTitle" + id);
    var windowTopOperateSpan = document.getElementById("windowTopOperate" + id);
    var windowTopCloseSpan = document.getElementById("windowTopClose" + id);
    var windowTopDiv = document.getElementById("windowTop" + id);
    var windowTopBgDiv = document.getElementById("windowTopBg" + id);
    var windowContentDiv = document.getElementById("windowContent" + id);
    var bgObj = document.getElementById("window" + id);

    if (windowTopDiv != null) {
        windowTopDiv.removeChild(windowTopTitleSpan);
        windowTopDiv.removeChild(windowTopOperateSpan);
        windowTopDiv.removeChild(windowTopCloseSpan);
        windowTopBgDiv.removeChild(windowTopDiv);
    }
    windowTopBgDiv.removeChild(windowContentDiv);
    document.body.removeChild(windowTopBgDiv);
    document.body.removeChild(bgObj);
}