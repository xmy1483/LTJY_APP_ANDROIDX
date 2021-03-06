// { "framework": "Weex" }
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

	var __weex_template__ = __webpack_require__(1)
	var __weex_style__ = __webpack_require__(2)
	var __weex_script__ = __webpack_require__(3)

	__weex_define__('@weex-component/0dfa4e1b06c4b7a2a137f5336efc874a', [], function(__weex_require__, __weex_exports__, __weex_module__) {

	    __weex_script__(__weex_module__, __weex_exports__, __weex_require__)
	    if (__weex_exports__.__esModule && __weex_exports__.default) {
	      __weex_module__.exports = __weex_exports__.default
	    }

	    __weex_module__.exports.template = __weex_template__

	    __weex_module__.exports.style = __weex_style__

	})

	__weex_bootstrap__('@weex-component/0dfa4e1b06c4b7a2a137f5336efc874a',undefined,undefined)

/***/ }),
/* 1 */
/***/ (function(module, exports) {

	module.exports = {
	  "type": "div",
	  "children": [
	    {
	      "type": "text",
	      "classList": [
	        "text"
	      ],
	      "style": {
	        "color": "#FF0000"
	      },
	      "attr": {
	        "value": "Hello world"
	      }
	    },
	    {
	      "type": "text",
	      "repeat": {
	        "expression": function () {return this.items},
	        "value": "item"
	      },
	      "classList": function () {return ['text', this.item.sex]},
	      "attr": {
	        "value": function () {return this.item.name}
	      }
	    }
	  ]
	}

/***/ }),
/* 2 */
/***/ (function(module, exports) {

	module.exports = {
	  "text": {
	    "fontSize": 80,
	    "textAlign": "center",
	    "width": 750,
	    "marginTop": 40,
	    "marginBottom": 40,
	    "borderRadius": 30,
	    "justifyContent": "center",
	    "color": "#ffffff"
	  },
	  "male": {
	    "backgroundColor": "#9999ff"
	  },
	  "female": {
	    "backgroundColor": "#ff9999"
	  }
	}

/***/ }),
/* 3 */
/***/ (function(module, exports) {

	module.exports = function(module, exports, __weex_require__){"use strict";

	module.exports = {
		data: function () {return {
			items: [{ name: "xy", sex: "female" }, { name: "xd", sex: "male" }]
		}},
		methods: {}
	};}
	/* generated by weex-loader */


/***/ })
/******/ ]);