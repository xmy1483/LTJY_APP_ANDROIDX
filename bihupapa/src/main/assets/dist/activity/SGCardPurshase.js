// { "framework": "Weex"} 

!function(t){function e(n){if(i[n])return i[n].exports;var o=i[n]={i:n,l:!1,exports:{}};return t[n].call(o.exports,o,o.exports,e),o.l=!0,o.exports}var i={};e.m=t,e.c=i,e.i=function(t){return t},e.d=function(t,i,n){e.o(t,i)||Object.defineProperty(t,i,{configurable:!1,enumerable:!0,get:n})},e.n=function(t){var i=t&&t.__esModule?function(){return t.default}:function(){return t};return e.d(i,"a",i),i},e.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},e.p="",e(e.s=168)}([function(t,e,i){t.exports=!i(1)(function(){return 7!=Object.defineProperty({},"a",{get:function(){return 7}}).a})},function(t,e){t.exports=function(t){try{return!!t()}catch(t){return!0}}},function(t,e){var i=t.exports="undefined"!=typeof window&&window.Math==Math?window:"undefined"!=typeof self&&self.Math==Math?self:Function("return this")();"number"==typeof __g&&(__g=i)},function(t,e){t.exports=function(t){return"object"==typeof t?null!==t:"function"==typeof t}},function(t,e){var i=t.exports={version:"2.4.0"};"number"==typeof __e&&(__e=i)},function(t,e){t.exports=function(t){if(void 0==t)throw TypeError("Can't call method on  "+t);return t}},function(t,e,i){var n=i(18);t.exports=Object("z").propertyIsEnumerable(0)?Object:function(t){return"String"==n(t)?t.split(""):Object(t)}},function(t,e){var i=Math.ceil,n=Math.floor;t.exports=function(t){return isNaN(t=+t)?0:(t>0?n:i)(t)}},function(t,e,i){var n=i(6),o=i(5);t.exports=function(t){return n(o(t))}},function(t,e,i){var n=i(65),o=i(54),r=i(76);__weex_define__("@weex-component/wxc-navbar",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(69),o=i(58),r=i(80);__weex_define__("@weex-component/wxc-tabitem",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(2),o=i(4),r=i(19),s=i(23),a=function(t,e,i){var c,u,l,f=t&a.F,d=t&a.G,p=t&a.S,h=t&a.P,g=t&a.B,m=t&a.W,y=d?o:o[e]||(o[e]={}),x=y.prototype,b=d?n:p?n[e]:(n[e]||{}).prototype;d&&(i=e);for(c in i)(u=!f&&b&&void 0!==b[c])&&c in y||(l=u?b[c]:i[c],y[c]=d&&"function"!=typeof b[c]?i[c]:g&&u?r(l,n):m&&b[c]==l?function(t){var e=function(e,i,n){if(this instanceof t){switch(arguments.length){case 0:return new t;case 1:return new t(e);case 2:return new t(e,i)}return new t(e,i,n)}return t.apply(this,arguments)};return e.prototype=t.prototype,e}(l):h&&"function"==typeof l?r(Function.call,l):l,h&&((y.virtual||(y.virtual={}))[c]=l,t&a.R&&x&&!x[c]&&s(x,c,l)))};a.F=1,a.G=2,a.S=4,a.P=8,a.B=16,a.W=32,a.U=64,a.R=128,t.exports=a},function(t,e,i){var n=i(16),o=i(24),r=i(36),s=Object.defineProperty;e.f=i(0)?Object.defineProperty:function(t,e,i){if(n(t),e=r(e,!0),n(i),o)try{return s(t,e,i)}catch(t){}if("get"in i||"set"in i)throw TypeError("Accessors not supported!");return"value"in i&&(t[e]=i.value),t}},function(t,e,i){t.exports={default:i(14),__esModule:!0}},function(t,e,i){i(38),t.exports=i(4).Object.assign},function(t,e){t.exports=function(t){if("function"!=typeof t)throw TypeError(t+" is not a function!");return t}},function(t,e,i){var n=i(3);t.exports=function(t){if(!n(t))throw TypeError(t+" is not an object!");return t}},function(t,e,i){var n=i(8),o=i(34),r=i(33);t.exports=function(t){return function(e,i,s){var a,c=n(e),u=o(c.length),l=r(s,u);if(t&&i!=i){for(;u>l;)if((a=c[l++])!=a)return!0}else for(;u>l;l++)if((t||l in c)&&c[l]===i)return t||l||0;return!t&&-1}}},function(t,e){var i={}.toString;t.exports=function(t){return i.call(t).slice(8,-1)}},function(t,e,i){var n=i(15);t.exports=function(t,e,i){if(n(t),void 0===e)return t;switch(i){case 1:return function(i){return t.call(e,i)};case 2:return function(i,n){return t.call(e,i,n)};case 3:return function(i,n,o){return t.call(e,i,n,o)}}return function(){return t.apply(e,arguments)}}},function(t,e,i){var n=i(3),o=i(2).document,r=n(o)&&n(o.createElement);t.exports=function(t){return r?o.createElement(t):{}}},function(t,e){t.exports="constructor,hasOwnProperty,isPrototypeOf,propertyIsEnumerable,toLocaleString,toString,valueOf".split(",")},function(t,e){var i={}.hasOwnProperty;t.exports=function(t,e){return i.call(t,e)}},function(t,e,i){var n=i(12),o=i(30);t.exports=i(0)?function(t,e,i){return n.f(t,e,o(1,i))}:function(t,e,i){return t[e]=i,t}},function(t,e,i){t.exports=!i(0)&&!i(1)(function(){return 7!=Object.defineProperty(i(20)("div"),"a",{get:function(){return 7}}).a})},function(t,e,i){"use strict";var n=i(28),o=i(26),r=i(29),s=i(35),a=i(6),c=Object.assign;t.exports=!c||i(1)(function(){var t={},e={},i=Symbol(),n="abcdefghijklmnopqrst";return t[i]=7,n.split("").forEach(function(t){e[t]=t}),7!=c({},t)[i]||Object.keys(c({},e)).join("")!=n})?function(t,e){for(var i=s(t),c=arguments.length,u=1,l=o.f,f=r.f;c>u;)for(var d,p=a(arguments[u++]),h=l?n(p).concat(l(p)):n(p),g=h.length,m=0;g>m;)f.call(p,d=h[m++])&&(i[d]=p[d]);return i}:c},function(t,e){e.f=Object.getOwnPropertySymbols},function(t,e,i){var n=i(22),o=i(8),r=i(17)(!1),s=i(31)("IE_PROTO");t.exports=function(t,e){var i,a=o(t),c=0,u=[];for(i in a)i!=s&&n(a,i)&&u.push(i);for(;e.length>c;)n(a,i=e[c++])&&(~r(u,i)||u.push(i));return u}},function(t,e,i){var n=i(27),o=i(21);t.exports=Object.keys||function(t){return n(t,o)}},function(t,e){e.f={}.propertyIsEnumerable},function(t,e){t.exports=function(t,e){return{enumerable:!(1&t),configurable:!(2&t),writable:!(4&t),value:e}}},function(t,e,i){var n=i(32)("keys"),o=i(37);t.exports=function(t){return n[t]||(n[t]=o(t))}},function(t,e,i){var n=i(2),o=n["__core-js_shared__"]||(n["__core-js_shared__"]={});t.exports=function(t){return o[t]||(o[t]={})}},function(t,e,i){var n=i(7),o=Math.max,r=Math.min;t.exports=function(t,e){return t=n(t),t<0?o(t+e,0):r(t,e)}},function(t,e,i){var n=i(7),o=Math.min;t.exports=function(t){return t>0?o(n(t),9007199254740991):0}},function(t,e,i){var n=i(5);t.exports=function(t){return Object(n(t))}},function(t,e,i){var n=i(3);t.exports=function(t,e){if(!n(t))return t;var i,o;if(e&&"function"==typeof(i=t.toString)&&!n(o=i.call(t)))return o;if("function"==typeof(i=t.valueOf)&&!n(o=i.call(t)))return o;if(!e&&"function"==typeof(i=t.toString)&&!n(o=i.call(t)))return o;throw TypeError("Can't convert object to primitive value")}},function(t,e){var i=0,n=Math.random();t.exports=function(t){return"Symbol(".concat(void 0===t?"":t,")_",(++i+n).toString(36))}},function(t,e,i){var n=i(11);n(n.S+n.F,"Object",{assign:i(25)})},function(t,e,i){i(40),i(42),i(43),i(46),i(48),i(41),i(44),i(9),i(45),i(47),i(10)},function(t,e,i){var n=i(60),o=i(49),r=i(71);__weex_define__("@weex-component/wxc-button",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(61),o=i(50),r=i(72);__weex_define__("@weex-component/wxc-countdown",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(62),o=i(51),r=i(73);__weex_define__("@weex-component/wxc-hn",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(63),o=i(52),r=i(74);__weex_define__("@weex-component/wxc-list-item",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(64),o=i(53),r=i(75);__weex_define__("@weex-component/wxc-marquee",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){i(9);var n=i(66),o=i(55),r=i(77);__weex_define__("@weex-component/wxc-navpage",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(67),o=i(56),r=i(78);__weex_define__("@weex-component/wxc-panel",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){i(10);var n=i(68),o=i(57),r=i(79);__weex_define__("@weex-component/wxc-tabbar",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e,i){var n=i(70),o=i(59),r=i(81);__weex_define__("@weex-component/wxc-tip",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o})},function(t,e){t.exports={btn:{marginBottom:0,alignItems:"center",justifyContent:"center",borderWidth:1,borderStyle:"solid",borderColor:"#333333"},"btn-default":{color:"rgb(51,51,51)"},"btn-primary":{backgroundColor:"rgb(40,96,144)",borderColor:"rgb(40,96,144)"},"btn-success":{backgroundColor:"rgb(92,184,92)",borderColor:"rgb(76,174,76)"},"btn-info":{backgroundColor:"rgb(91,192,222)",borderColor:"rgb(70,184,218)"},"btn-warning":{backgroundColor:"rgb(240,173,78)",borderColor:"rgb(238,162,54)"},"btn-danger":{backgroundColor:"rgb(217,83,79)",borderColor:"rgb(212,63,58)"},"btn-link":{borderColor:"rgba(0,0,0,0)",borderRadius:0},"btn-txt-default":{color:"rgb(51,51,51)"},"btn-txt-primary":{color:"rgb(255,255,255)"},"btn-txt-success":{color:"rgb(255,255,255)"},"btn-txt-info":{color:"rgb(255,255,255)"},"btn-txt-warning":{color:"rgb(255,255,255)"},"btn-txt-danger":{color:"rgb(255,255,255)"},"btn-txt-link":{color:"rgb(51,122,183)"},"btn-sz-large":{width:300,height:100,paddingTop:25,paddingBottom:25,paddingLeft:40,paddingRight:40,borderRadius:15},"btn-sz-middle":{width:240,height:80,paddingTop:15,paddingBottom:15,paddingLeft:30,paddingRight:30,borderRadius:10},"btn-sz-small":{width:170,height:60,paddingTop:12,paddingBottom:12,paddingLeft:25,paddingRight:25,borderRadius:7},"btn-txt-sz-large":{fontSize:45},"btn-txt-sz-middle":{fontSize:35},"btn-txt-sz-small":{fontSize:30}}},function(t,e){t.exports={wrap:{overflow:"hidden"}}},function(t,e){t.exports={h1:{height:110,paddingTop:20,paddingBottom:20},h2:{height:110,paddingTop:20,paddingBottom:20},h3:{height:110,paddingTop:20,paddingBottom:20},"txt-h1":{fontSize:70},"txt-h2":{fontSize:52},"txt-h3":{fontSize:42}}},function(t,e){t.exports={item:{paddingTop:25,paddingBottom:25,paddingLeft:35,paddingRight:35,height:160,justifyContent:"center",borderBottomWidth:1,borderColor:"#dddddd"}}},function(t,e){t.exports={wrap:{overflow:"hidden",position:"relative"},anim:{flexDirection:"column",position:"absolute",transform:"translateY(0) translateZ(0)"}}},function(t,e){t.exports={container:{flexDirection:"row",position:"fixed",top:0,left:0,right:0,width:750},"right-text":{position:"absolute",bottom:28,right:32,textAlign:"right",fontSize:32,fontFamily:"'Open Sans', sans-serif"},"left-text":{position:"absolute",bottom:28,left:32,textAlign:"left",fontSize:32,fontFamily:"'Open Sans', sans-serif"},"center-text":{position:"absolute",bottom:25,left:172,right:172,textAlign:"center",fontSize:36,fontWeight:"bold"},"left-image":{position:"absolute",bottom:20,left:28,width:50,height:50},"right-image":{position:"absolute",bottom:20,right:28,width:50,height:50}}},function(t,e){t.exports={wrapper:{position:"absolute",top:0,left:0,right:0,bottom:0,width:750}}},function(t,e){t.exports={panel:{marginBottom:20,backgroundColor:"#ffffff",borderColor:"#dddddd",borderWidth:1},"panel-primary":{borderColor:"rgb(40,96,144)"},"panel-success":{borderColor:"rgb(76,174,76)"},"panel-info":{borderColor:"rgb(70,184,218)"},"panel-warning":{borderColor:"rgb(238,162,54)"},"panel-danger":{borderColor:"rgb(212,63,58)"},"panel-header":{backgroundColor:"#f5f5f5",fontSize:40,color:"#333333"},"panel-header-primary":{backgroundColor:"rgb(40,96,144)",color:"#ffffff"},"panel-header-success":{backgroundColor:"rgb(92,184,92)",color:"#ffffff"},"panel-header-info":{backgroundColor:"rgb(91,192,222)",color:"#ffffff"},"panel-header-warning":{backgroundColor:"rgb(240,173,78)",color:"#ffffff"},"panel-header-danger":{backgroundColor:"rgb(217,83,79)",color:"#ffffff"}}},function(t,e){t.exports={wrapper:{width:750,position:"absolute",top:0,left:0,right:0,bottom:0},content:{position:"absolute",top:0,left:0,right:0,bottom:0,marginTop:0,marginBottom:88},tabbar:{flexDirection:"row",position:"fixed",bottom:0,left:0,right:0,height:90}}},function(t,e){t.exports={container:{flex:1,flexDirection:"column",alignItems:"center",justifyContent:"center",height:90},"top-line":{position:"absolute",top:0,left:0,right:0,height:2},"tab-icon":{marginTop:5,width:45,height:45},"tab-text":{marginTop:5,textAlign:"center",fontSize:25}}},function(t,e){t.exports={tip:{paddingLeft:36,paddingRight:36,paddingTop:36,paddingBottom:36,borderRadius:10},"tip-txt":{fontSize:28},"tip-success":{backgroundColor:"#dff0d8",borderColor:"#d6e9c6"},"tip-txt-success":{color:"#3c763d"},"tip-info":{backgroundColor:"#d9edf7",borderColor:"#bce8f1"},"tip-txt-info":{color:"#31708f"},"tip-warning":{backgroundColor:"#fcf8e3",borderColor:"#faebcc"},"tip-txt-warning":{color:"#8a6d3b"},"tip-danger":{backgroundColor:"#f2dede",borderColor:"#ebccd1"},"tip-txt-danger":{color:"#a94442"}}},function(t,e){t.exports={type:"div",classList:function(){return["btn","btn-"+this.type,"btn-sz-"+this.size]},children:[{type:"text",classList:function(){return["btn-txt","btn-txt-"+this.type,"btn-txt-sz-"+this.size]},attr:{value:function(){return this.value}}}]}},function(t,e){t.exports={type:"div",style:{overflow:"hidden",flexDirection:"row"},events:{appear:"appeared",disappear:"disappeared"},children:[{type:"content"}]}},function(t,e){t.exports={type:"div",classList:function(){return["h"+this.level]},style:{justifyContent:"center"},children:[{type:"text",classList:function(){return["txt-h"+this.level]},attr:{value:function(){return this.value}}}]}},function(t,e){t.exports={type:"div",classList:["item"],events:{touchstart:"touchstart",touchend:"touchend"},style:{backgroundColor:function(){return this.bgColor}},children:[{type:"content"}]}},function(t,e){t.exports={type:"div",classList:["wrap"],events:{appear:"appeared",disappear:"disappeared"},children:[{type:"div",id:"anim",classList:["anim"],children:[{type:"content"}]}]}},function(t,e){t.exports={type:"div",classList:["container"],style:{height:function(){return this.height},backgroundColor:function(){return this.backgroundColor}},attr:{dataRole:function(){return this.dataRole}},children:[{type:"text",classList:["right-text"],style:{color:function(){return this.rightItemColor}},attr:{naviItemPosition:"right",value:function(){return this.rightItemTitle}},shown:function(){return!this.rightItemSrc},events:{click:"onclickrightitem"}},{type:"image",classList:["right-image"],attr:{naviItemPosition:"right",src:function(){return this.rightItemSrc}},shown:function(){return this.rightItemSrc},events:{click:"onclickrightitem"}},{type:"text",classList:["left-text"],style:{color:function(){return this.leftItemColor}},attr:{naviItemPosition:"left",value:function(){return this.leftItemTitle}},shown:function(){return!this.leftItemSrc},events:{click:"onclickleftitem"}},{type:"image",classList:["left-image"],attr:{naviItemPosition:"left",src:function(){return this.leftItemSrc}},shown:function(){return this.leftItemSrc},events:{click:"onclickleftitem"}},{type:"text",classList:["center-text"],style:{color:function(){return this.titleColor}},attr:{naviItemPosition:"center",value:function(){return this.title}}}]}},function(t,e){t.exports={type:"div",classList:["wrapper"],children:[{type:"wxc-navbar",attr:{dataRole:function(){return this.dataRole},height:function(){return this.height},backgroundColor:function(){return this.backgroundColor},title:function(){return this.title},titleColor:function(){return this.titleColor},leftItemSrc:function(){return this.leftItemSrc},leftItemTitle:function(){return this.leftItemTitle},leftItemColor:function(){return this.leftItemColor},rightItemSrc:function(){return this.rightItemSrc},rightItemTitle:function(){return this.rightItemTitle},rightItemColor:function(){return this.rightItemColor}}},{type:"div",classList:["wrapper"],style:{marginTop:function(){return this.height}},children:[{type:"content"}]}]}},function(t,e){t.exports={type:"div",classList:function(){return["panel","panel-"+this.type]},style:{borderWidth:function(){return this.border}},children:[{type:"text",classList:function(){return["panel-header","panel-header-"+this.type]},style:{paddingTop:function(){return this.paddingHead},paddingBottom:function(){return this.paddingHead},paddingLeft:function(){return 1.5*this.paddingHead},paddingRight:function(){return 1.5*this.paddingHead}},attr:{value:function(){return this.title}}},{type:"div",classList:function(){return["panel-body","panel-body-"+this.type]},style:{paddingTop:function(){return this.paddingBody},paddingBottom:function(){return this.paddingBody},paddingLeft:function(){return 1.5*this.paddingBody},paddingRight:function(){return 1.5*this.paddingBody}},children:[{type:"content"}]}]}},function(t,e){t.exports={type:"div",classList:["wrapper"],children:[{type:"embed",classList:["content"],style:{visibility:function(){return this.visibility}},repeat:function(){return this.tabItems},attr:{src:function(){return this.src},type:"weex"}},{type:"div",classList:["tabbar"],append:"tree",children:[{type:"wxc-tabitem",repeat:function(){return this.tabItems},attr:{index:function(){return this.index},icon:function(){return this.icon},title:function(){return this.title},titleColor:function(){return this.titleColor}}}]}]}},function(t,e){t.exports={type:"div",classList:["container"],style:{backgroundColor:function(){return this.backgroundColor}},events:{click:"onclickitem"},children:[{type:"image",classList:["top-line"],attr:{src:"http://gtms03.alicdn.com/tps/i3/TB1mdsiMpXXXXXpXXXXNw4JIXXX-640-4.png"}},{type:"image",classList:["tab-icon"],attr:{src:function(){return this.icon}}},{type:"text",classList:["tab-text"],style:{color:function(){return this.titleColor}},attr:{value:function(){return this.title}}}]}},function(t,e){t.exports={type:"div",classList:function(){return["tip","tip-"+this.type]},children:[{type:"text",classList:function(){return["tip-txt","tip-txt-"+this.type]},attr:{value:function(){return this.value}}}]}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{type:"default",size:"large",value:""}},methods:{}}}},function(t,e,i){t.exports=function(t,e,n){"use strict";var o=i(13),r=function(t){return t&&t.__esModule?t:{default:t}}(o);t.exports={data:function(){return{now:0,remain:0,time:{elapse:0,D:"0",DD:"0",h:"0",hh:"00",H:"0",HH:"0",m:"0",mm:"00",M:"0",MM:"0",s:"0",ss:"00",S:"0",SS:"0"},outofview:!1}},ready:function(){this.remain<=0||(this.now=Date.now(),this.nextTick())},methods:{nextTick:function(){this.outofview?setTimeout(this.nextTick.bind(this),1e3):(this.time.elapse=parseInt((Date.now()-this.now)/1e3),this.calc()?(this.$emit("tick",(0,r.default)({},this.time)),setTimeout(this.nextTick.bind(this),1e3)):this.$emit("alarm",(0,r.default)({},this.time)),this._app.updateActions())},format:function(t){return t.length>=2?t:"0"+t},calc:function(){var t=this.remain-this.time.elapse;return t<0&&(t=0),this.time.D=String(parseInt(t/86400)),this.time.DD=this.format(this.time.D),this.time.h=String(parseInt((t-86400*parseInt(this.time.D))/3600)),this.time.hh=this.format(this.time.h),this.time.H=String(parseInt(t/3600)),this.time.HH=this.format(this.time.H),this.time.m=String(parseInt((t-3600*parseInt(this.time.H))/60)),this.time.mm=this.format(this.time.m),this.time.M=String(parseInt(t/60)),this.time.MM=this.format(this.time.M),this.time.s=String(t-60*parseInt(this.time.M)),this.time.ss=this.format(this.time.s),this.time.S=String(t),this.time.SS=this.format(this.time.S),t>0},appeared:function(){this.outofview=!1},disappeared:function(){this.outofview=!0}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{level:1,value:""}},methods:{}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{bgColor:"#ffffff"}},methods:{touchstart:function(){},touchend:function(){}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{step:0,count:0,index:1,duration:0,interval:0,outofview:!1}},ready:function(){this.interval>0&&this.step>0&&this.duration>0&&this.nextTick()},methods:{nextTick:function(){var t=this;this.outofview?setTimeout(t.nextTick.bind(t),t.interval):setTimeout(function(){t.animation(t.nextTick.bind(t))},t.interval)},animation:function(t){var e=this,n=-e.step*e.index;i("@weex-module/animation").transition(this.$el("anim"),{styles:{transform:"translateY("+String(n)+"px) translateZ(0)"},timingFunction:"ease",duration:e.duration},function(){e.index=(e.index+1)%e.count,e.$emit("change",{index:e.index,count:e.count}),t&&t()})},appeared:function(){this.outofview=!1},disappeared:function(){this.outofview=!0}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{dataRole:"navbar",backgroundColor:"black",height:88,title:"",titleColor:"black",rightItemSrc:"",rightItemTitle:"",rightItemColor:"black",leftItemSrc:"",leftItemTitle:"",leftItemColor:"black"}},methods:{onclickrightitem:function(t){this.$dispatch("naviBar.rightItem.click",{})},onclickleftitem:function(t){this.$dispatch("naviBar.leftItem.click",{})}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{dataRole:"navbar",backgroundColor:"black",height:88,title:"",titleColor:"black",rightItemSrc:"",rightItemTitle:"",rightItemColor:"black",leftItemSrc:"",leftItemTitle:"",leftItemColor:"black"}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{type:"default",title:"",paddingBody:20,paddingHead:20,dataClass:"",border:0}},ready:function(){}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{tabItems:[],selectedIndex:0,selectedColor:"#ff0000",unselectedColor:"#000000"}},created:function(){this.selected(this.selectedIndex),this.$on("tabItem.onClick",function(t){var e=t.detail;this.selectedIndex=e.index,this.selected(e.index);var i={index:e.index};this.$dispatch("tabBar.onClick",i)})},methods:{selected:function(t){for(var e=0;e<this.tabItems.length;e++){var i=this.tabItems[e];e==t?(i.icon=i.selectedImage,i.titleColor=this.selectedColor,i.visibility="visible"):(i.icon=i.image,i.titleColor=this.unselectedColor,i.visibility="hidden")}}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{index:0,title:"",titleColor:"#000000",icon:"",backgroundColor:"#ffffff"}},methods:{onclickitem:function(t){var e=this,i={index:e.index};e.$dispatch("tabItem.onClick",i)}}}}},function(t,e){t.exports=function(t,e,i){"use strict";t.exports={data:function(){return{type:"success",value:""}}}}},,function(t,e,i){t.exports={default:i(84),__esModule:!0}},function(t,e,i){var n=i(4),o=n.JSON||(n.JSON={stringify:JSON.stringify});t.exports=function(t){return o.stringify.apply(o,arguments)}},function(t,e){t.exports={container:{display:"flex",flexDirection:"column",backgroundColor:"#EFEFE3",justifyContent:"space-between"},titleImg:{height:80},segment:{height:2},chooseLine:{flexDirection:"row",paddingLeft:20,paddingTop:10,paddingBottom:10,backgroundColor:"#ffffff",justifyContent:"space-between",height:70},cardMoney:{lineHeight:50},amountItem:{flexDirection:"row",justifyContent:"space-around",marginRight:20},minus:{width:80,color:"#ffffff",borderRadius:5,textAlign:"center",lineHeight:40},amounts:{width:120,marginLeft:10,marginRight:10,borderWidth:2,borderColor:"#DBDBDB",borderRadius:5,textAlign:"center",lineHeight:40},add:{width:80,color:"#ffffff",borderRadius:5,textAlign:"center",lineHeight:40},postageItem:{flexDirection:"row",justifyContent:"space-between",paddingLeft:10,paddingRight:10,backgroundColor:"#ffffff"},postageTip:{marginTop:20,marginBottom:20,paddingTop:20,paddingBottom:20},postageMoney:{borderColor:"#3A8EE2",borderWidth:3,borderRadius:10,marginTop:20,marginBottom:20,paddingLeft:20,paddingRight:20,lineHeight:80,color:"#3A8EE2"},totalCount:{flexDirection:"row",justifyContent:"space-between",paddingLeft:20,paddingRight:20,paddingTop:25,paddingBottom:25,marginBottom:20,backgroundColor:"#ffffff"},payStyle:{display:"flex",flexDirection:"column"},blank:{flexDirection:"row",alignItems:"center",justifyContent:"center",backgroundColor:"#FFFFFF"},icon:{height:110,flex:1},balanceMoney:{flex:1,height:120,fontSize:32,paddingTop:40,paddingBottom:40},check:{height:60},cherk:{flex:.2,paddingRight:20},payItem:{flexDirection:"row",justifyContent:"space-between",marginTop:20,backgroundColor:"#ffffff"},ShowMoney:{marginLeft:20,paddingTop:35,paddingBottom:35},PayButton:{backgroundColor:"#3A8EE2",paddingLeft:100,paddingRight:100,marginRight:20,marginTop:10,marginBottom:10,color:"#ffffff",lineHeight:90,borderRadius:10}}},,,,,,,,,,,,,,,,,,,,,,,,,function(t,e){t.exports={type:"wxc-navpage",attr:{dataRole:"none",backgroundColor:"#3A8EE2",height:function(){return this.height},title:function(){return this.title},titleColor:"#ffffff",leftItemSrc:"https://app5.bac365.com:10443/web_image/ic_arrow_nor@2x.png"},children:[{type:"div",classList:["container"],style:{height:function(){return this.bodyHeight}},children:[{type:"scroller",children:[{type:"div",classList:["title"],children:[{type:"image",attr:{src:function(){return this.titleImg}},classList:["titleImg"]}]},{type:"div",repeat:{expression:function(){return this.items},value:"item"},classList:["chooseList"],children:[{type:"image",attr:{src:"https://app5.bac365.com:10443/weex/jd/JD_card/segmentLine.png"},classList:["segment"]},{type:"div",classList:["chooseLine"],children:[{type:"text",classList:["cardMoney"],style:{fontSize:35},attr:{value:function(){return this.item.gift_value+"元面值"}}},{type:"div",classList:["amountItem"],children:[{type:"text",classList:["minus"],events:{click:function(t){this.min(this.$index,t)}},style:{backgroundColor:function(){return this.item.minusBackColor},fontSize:35},attr:{value:"-"}},{type:"text",classList:["amounts"],style:{color:function(){return this.item.amountColor},fontSize:35},attr:{value:function(){return this.item.amount}}},{type:"text",classList:["add"],events:{click:function(t){this.add(this.$index,t)}},style:{backgroundColor:function(){return this.item.addBackColor},fontSize:35},attr:{value:"+"}}]}]}]},{type:"image",attr:{src:"https://app5.bac365.com:10443/weex/jd/JD_card/segmentLine.png"},classList:["segment"]},{type:"div",classList:["totalCount"],children:[{type:"text",style:{fontSize:32},attr:{value:function(){return"共计"+this.totalMount+"件商品"}}},{type:"text",style:{color:"#3A8EE2",fontSize:32},attr:{value:function(){return"折扣:"+this.disCount}}},{type:"text",style:{fontSize:32},attr:{value:function(){return"小计: ¥"+this.totalPrice}}}]},{type:"div",classList:["postageItem"],attr:{backgroundColor:"#ffffff"},children:[{type:"text",classList:["postageTip"],style:{fontSize:32},attr:{value:function(){return"最高"+this.miniDiscount+"折,满"+this.delivery_standard_max+"元包邮"}}},{type:"text",classList:["postageMoney"],style:{fontSize:32},attr:{value:function(){return"快递费¥"+this.postMoney+"元"}}}]},{type:"div",repeat:{expression:function(){return this.payItems},value:"payItem"},classList:function(){return["payStyle",this.payItem.title]},events:{click:function(t){this.select(this.$index,t)}},children:[{type:"image",attr:{src:"https://app5.bac365.com:10443/weex/jd/JD_card/segmentLine.png"},classList:["segment"]},{type:"div",classList:["blank"],children:[{type:"image",attr:{src:function(){return this.payItem.src}},classList:["icon"]},{type:"text",classList:["balanceMoney"],attr:{value:function(){return this.payItem.text}}},{type:"div",classList:["cherk"],children:[{type:"image",attr:{src:function(){return this.check}},shown:function(){return this.payItem.show},classList:["check"]}]}]}]},{type:"image",attr:{src:"https://app5.bac365.com:10443/weex/jd/JD_card/segmentLine.png"},classList:["segment"]}]},{type:"div",classList:["payItem"],children:[{type:"text",classList:["ShowMoney"],style:{fontSize:35},attr:{value:function(){return"合计：¥"+this.allTotalMoney+"元"}}},{type:"text",classList:["PayButton"],events:{click:"confirm"},style:{fontSize:35},attr:{value:"支付"}}]}]}]}},,,,,,,,,,,,,,,,,,,,,,,,,,,function(t,e,i){t.exports=function(t,e,n){"use strict";var o=i(83),r=function(t){return t&&t.__esModule?t:{default:t}}(o),s=weex.requireModule("storage"),a=weex.requireModule("modal"),c=i(!function(){var t=new Error('Cannot find module "./api"');throw t.code="MODULE_NOT_FOUND",t}()),u=weex.requireModule("weexSGModule");i(39),t.exports={data:function(){return{title:"苏果购物卡",height:128,titleImg:"https://app5.bac365.com:10443/weex/sg/SGCard/SGlogo@3x.png",orderMoney:"5",kyb:0,is_have_pay_pw:!1,cardList:[],bodyHeight:600,items:[{gift_value:"50",isOutOfStock:!0,gift_id:"111111111",gift_type:1,gift_num:0,gift_stock:0,gift_discount:1,amount:"缺货",minusBackColor:"#3A8EE2",amountColor:"#DBDBDB",addBackColor:"#3A8EE2"},{gift_value:"100",isOutOfStock:!1,gift_id:"111111111",gift_type:1,gift_num:0,gift_stock:0,gift_discount:1,amount:"0",minusBackColor:"#3A8EE2",amountColor:"#DBDBDB",addBackColor:"#3A8EE2"},{gift_value:"100",isOutOfStock:!1,gift_id:"111111111",gift_type:1,gift_num:0,gift_stock:0,gift_discount:1,amount:"0",minusBackColor:"#3A8EE2",amountColor:"#DBDBDB",addBackColor:"#3A8EE2"},{gift_value:"100",isOutOfStock:!1,gift_id:"111111111",gift_type:1,gift_num:0,gift_stock:0,gift_discount:1,amount:"0",minusBackColor:"#3A8EE2",amountColor:"#DBDBDB",addBackColor:"#3A8EE2"},{gift_value:"100",isOutOfStock:!1,gift_id:"111111111",gift_type:1,gift_num:0,gift_stock:0,gift_discount:1,amount:"0",minusBackColor:"#3A8EE2",amountColor:"#DBDBDB",addBackColor:"#3A8EE2"}],totalMount:"0",disCount:"1.00",miniDiscount:"0.97",totalPrice:"0.00",unDisCountMoney:"0.00",disCountRule:[],delivery_standard_max:0,postMoney:5,delivery_fee:5,allTotalMoney:"0.00",payItems:[{src:"https://app5.bac365.com:10443/weex/jd/JD_card/kyb.png",text:"余额 0.00元",show:!1},{src:"https://app5.bac365.com:10443/weex/jd/JD_card/wechat.png",text:"",show:!1},{src:"https://app5.bac365.com:10443/weex/jd/JD_card/alipay.png",text:"",show:!0}],check:"https://app5.bac365.com:10443/weex/jd/JD_card/Checkmark.png"}},methods:{min:function(t){if("缺货"==this.items[t].amount);else if("0"==this.items[t].amount);else{this.unDisCountMoney=parseFloat(this.unDisCountMoney)-parseFloat(this.items[t].gift_value);var e=c.getDiscount((0,r.default)(this.disCountRule),this.unDisCountMoney);this.disCount=parseFloat(e).toFixed(3);var i=this.items[t].amount;"1"==i&&(this.items[t].minusBackColor="#DDDDDD",this.items[t].amountColor="#DBDBDB");var n=this.items[t];if(n.amount=parseInt(i)-1,this.items[t]=n,this.totalMount=parseInt(this.totalMount)-1,this.totalPrice=parseFloat(this.unDisCountMoney)*parseFloat(this.disCount),parseFloat(this.totalPrice)<0&&(this.totalPrice="0.00"),parseFloat(this.unDisCountMoney)<0&&(this.unDisCountMoney="0.00"),this.items[t].gift_num=parseInt(this.items[t].gift_num)-1,this.items[t].gift_num<0&&(this.items[t].gift_num=0),parseFloat(this.unDisCountMoney)<parseFloat(this.delivery_standard_max)){this.postMoney=this.delivery_fee;for(var o=0;o<this.items.length;o++)this.items[o].delivery_fee=this.postMoney}0==this.totalMount?this.allTotalMoney=0:this.allTotalMoney=parseFloat(this.totalPrice)+parseFloat(this.postMoney)}},add:function(t){var e=this;if("缺货"==this.items[t].amount);else if(this.items[t].amount==e.items[t].gift_stock)a.toast({message:"已达最大库存，无法添加",duration:1});else{this.unDisCountMoney=parseFloat(this.unDisCountMoney)+parseFloat(this.items[t].gift_value);var i=c.getDiscount((0,r.default)(this.disCountRule),this.unDisCountMoney);this.disCount=parseFloat(i).toFixed(3);var n=e.items[t].amount;"0"==n&&(this.items[t].minusBackColor="#3A8EE2",this.items[t].amountColor="#000000");var o=this.items[t];if(o.amount=parseInt(n)+1,this.items[t]=o,this.totalMount=parseInt(this.totalMount)+1,this.totalPrice=parseFloat(this.unDisCountMoney)*parseFloat(this.disCount),this.items[t].gift_num=parseInt(this.items[t].gift_num)+1,parseInt(this.items[t].gift_num)>parseInt(this.items[t].gift_stock)&&0!=parseInt(this.items[t].gift_stock)&&(this.items[t].gift_num=parseInt(this.items[t].gift_stock)),parseFloat(this.unDisCountMoney)>=parseFloat(this.delivery_standard_max)){this.postMoney=0;for(var s=0;s<this.items.length;s++)this.items[s].delivery_fee=this.postMoney}this.allTotalMoney=parseFloat(this.totalPrice)+parseFloat(this.postMoney)}},select:function(t){s.setItem("index",t);for(var e=this.payItems,i=0;i<e.length;i++)e[i].show=!1;e[t].show=!0,this.payItems=prePayItem},confirm:function(){var t=this,e=this;if("0"==e.totalMount)return void a.toast({message:"请选择礼品卡",duration:1});s.getItem("index",function(i){for(var n=[],o=0;o<t.items.length;o++)t.items[o].gift_num>0&&n.push({gift_id:t.items[o].gift_id,gift_value:t.items[o].gift_value,gift_type:t.items[o].gift_type,gift_num:t.items[o].gift_num});var r=e.cardList[0];r.gift_discount=t.disCount,r.delivery_fee=t.items[0].delivery_fee,r.gifts=n;for(var s=0,c=0,l=0,o=0;o<n.length;o++)s+=n[o].gift_value*n[o].gift_num*r.gift_discount,c+=n[o].gift_value*n[o].gift_num,l+=n[o].gift_value*n[o].gift_num;s=parseFloat(s)+parseFloat(r.delivery_fee),r.pay_money=s,r.recharge_money=c,r.sale_money=l,0==i.data?parseFloat(e.allTotalMoney)<=e.kyb?0==e.is_have_pay_pw?u.pushNativeController(JSON.parse({ViewController:"\tSetPayPasswordController"})):u.showPayPageView(r):a.toast({message:"支付金额大于余额，无法支付",duration:1}):1==i.data?u.payMoneyActually(r,"WECHAT"):2==i.data&&u.payMoneyActually(r,"ALIPAY")})}},created:function(){s.setItem("index",2);var t=this,e=this.$getConfig().env.deviceWidth,i=this.$getConfig().env.deviceHeight,n=750/e*i,o=n-128;t.bodyHeight=o,t.$on("naviBar.leftItem.click",function(e){var i={animated:"true"};t.$call("navigator","pop",i,function(){})});var r=u.getCurrentPhone(),a={methodName:"CARD_XML.QUERY_ACCOUNT_BALANCE",actionType:0,listMap:[{login_phone:r}]},c=u.getCustomersID(),l={methodName:"GIFT_XML.QUERY_GIFT_BY_TYPE",actionType:0,listMap:[{login_phone:r,customers_id:c,gift_type:1}]},f={methodName:"GIFT_XML.QUERY_GIFT_DISCOUNT",actionType:0,listMap:[{login_phone:r,customers_id:c,gift_type:1}]},t=this;u.requestWithParams(a,function(e){t.payItems[0].text="余额"+e.data[0].balance+"元";var i=e.data[0].is_have_pay_pw;t.kyb=e.data[0].balance,t.is_have_pay_pw=i,u.requestWithParams(l,function(e){for(var i=[],n=0;n<e.data.length;n++){var o=e.data[n];i.push({gift_value:o.gift_value,gift_id:o.gift_id,gift_type:o.gift_type,gift_num:"0",delivery_fee:o.delivery_fee,gift_stock:o.gift_stock,gift_discount:o.gift_discount,isOutOfStock:o.gift_stock>0,amount:o.gift_stock>0?"0":"缺货",minusBackColor:"#DDDDDD",amountColor:"#DBDBDB",addBackColor:o.gift_stock>0?"#3A8EE2":"#DDDDDD"})}t.items=i,t.disCount="1.00",t.miniDiscount=100*i[0].gift_discount,t.delivery_fee=i[0].delivery_fee,t.postMoney=i[0].delivery_fee,t.delivery_standard_max=e.data[0].delivery_standard_max,t.cardList=e.data,u.requestWithParams(f,function(e){t.disCountRule=e.data,t.add(parseInt(t.items.length-1))})})}),t.height="iOS"==t.$getConfig().env.platform?128:100}}}},,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,function(t,e,i){var n=i(110),o=i(85),r=i(137);__weex_define__("@weex-component/817b4915e0c85a0ab13c22c2f9559c6d",[],function(t,e,i){r(i,e,t),e.__esModule&&e.default&&(i.exports=e.default),i.exports.template=n,i.exports.style=o}),__weex_bootstrap__("@weex-component/817b4915e0c85a0ab13c22c2f9559c6d",void 0,void 0)}]);