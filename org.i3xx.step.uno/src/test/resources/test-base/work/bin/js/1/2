i3xx.feature( "i3xx.json" );
i3xx.require( "i3xx.i3xx" );

/*
 * code is edited to fit in program
 * for( avr i in arr ) for Arrays does not work with this implementation
 * 
 * code is taken from reference Implementation 
 * http://www.json.org/json.js
 * 
 * Author: Benny Boers, 30.08.2007 
 */

/**
 * i3xx.fromJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 16:26:19
 *
    This method parses a JSON text to produce an object or
    array. It can throw a SyntaxError exception.

    The optional filter parameter is a function which can filter and
    transform the results. It receives each of the keys and values, and
    its return value is used instead of the original value. If it
    returns what it received, then structure is not modified. If it
    returns undefined then the member is deleted.

    Example:

    // Parse the text. If a key contains the string 'date' then
    // convert the value to a date.

    myData = i3xx.fromJson( text, function (key, value) {
        return key.indexOf('date') >= 0 ? new Date(value) : value;
    });
 * 
 */
i3xx.define( "i3xx._fromJson", function( str, filter ){ //old method is replaced by new one
            var j;

            function walk(k, v) {
                var i;
                if (v && typeof v === 'object') {
                    for (i in v) {
                        if (Object.prototype.hasOwnProperty.apply(v, [i])) {
                            v[i] = walk(i, v[i]);
                        }
                    }
                }
                return filter(k, v);
            }


// Parsing happens in three stages. In the first stage, we run the text against
// a regular expression which looks for non-JSON characters. We are especially
// concerned with '()' and 'new' because they can cause invocation, and '='
// because it can cause mutation. But just to be safe, we will reject all
// unexpected characters.

// We split the first stage into 3 regexp operations in order to work around
// crippling deficiencies in Safari's regexp engine. First we replace all
// backslash pairs with '@' (a non-JSON character). Second we delete all of
// the string literals. Third, we look to see if only JSON characters
// remain. If so, then the text is safe for eval.
out.println("t0: "+str.
                    replace(/\\./g, '@').
                    replace(/"[^"\\\n\r]*"/g, ''));
out.println("t1: "+/^[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]*$/.test(str.
                    replace(/\\./g, '@').
                    replace(/"[^"\\\n\r]*"/g, '')));
            if (/^[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]*$/.test(str.
                    replace(/\\./g, '@').
                    replace(/"[^"\\\n\r]*"/g, ''))) {

// In the second stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.
out.println("t: "+str);
                j = eval('(' + str + ')');
out.println("j: "+j);

// In the optional third stage, we recursively walk the new structure, passing
// each name/value pair to a filter function for possible transformation.

                return typeof filter === 'function' ? walk('', j) : j;
            }

// If the text is not JSON parseable, then a SyntaxError is thrown.

            throw new SyntaxError('parseJSON');
});

i3xx.define("i3xx.json.cx", /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g);
i3xx.define("i3xx.json.escapeable", /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g);
i3xx.define("i3xx.json.gap", null);
i3xx.define("i3xx.json.indent", null);
i3xx.define("i3xx.json.meta", {    // table of character substitutions
                '\b': '\\b',
                '\t': '\\t',
                '\n': '\\n',
                '\f': '\\f',
                '\r': '\\r',
                '"' : '\\"',
                '\\': '\\\\'
            });
i3xx.define("i3xx.json.rep", null);

i3xx.define( "i3xx.fromJson", function( text, reviver ){
// The parse method takes a text and an optional reviver function, and returns
// a JavaScript value if the text is a valid JSON text.

                var j;

                function walk(holder, key) {

// The walk method is used to recursively walk the resulting structure so
// that modifications can be made.

                    var k, v, value = holder[key];
                    if (value && typeof value === 'object') {
                        for (k in value) {
                            if (Object.hasOwnProperty.call(value, k)) {
                                v = walk(value, k);
                                if (v !== undefined) {
                                    value[k] = v;
                                } else {
                                    delete value[k];
                                }
                            }
                        }
                    }
                    return reviver.call(holder, key, value);
                }


// Parsing happens in four stages. In the first stage, we replace certain
// Unicode characters with escape sequences. JavaScript handles many characters
// incorrectly, either silently deleting them, or treating them as line endings.
				
				

                i3xx.json.cx.lastIndex = 0;
                if (i3xx.json.cx.test(text)) {
                    text = text.replace(i3xx.json.cx, function (a) {
                        return '\\u' + ('0000' +
                                (+(a.charCodeAt(0))).toString(16)).slice(-4);
                    });
                }

// In the second stage, we run the text against regular expressions that look
// for non-JSON patterns. We are especially concerned with '()' and 'new'
// because they can cause invocation, and '=' because it can cause mutation.
// But just to be safe, we want to reject all unexpected forms.

// We split the second stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace the JSON backslash pairs with '@' (a non-JSON character). Second, we
// replace all simple value tokens with ']' characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or ']' or
// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

//ATTENTION: check is disabled, because of unsatisfying support of quoteless names in object string

//                if (/^[\],:{}\s]*$/.
//test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
//replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
//replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

// In the third stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                    j = eval('(' + text + ')');

// In the optional fourth stage, we recursively walk the new structure, passing
// each name/value pair to a reviver function for possible transformation.

                    return typeof reviver === 'function' ?
                        walk({'': j}, '') : j;
//                }

// If the text is not JSON parseable, then a SyntaxError is thrown.
//            throw new SyntaxError('parseJSON');
});
 
 
 
 
/**
 * i3xx.toJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:52:17
 */
i3xx.define( "i3xx.toJson", function( obj, w ){
	if( obj == null )
		return "";
		
	var ret = "";	
	if( typeof obj == "object" && obj instanceof Array ) {
		ret = i3xx.ArrayToJson( obj, w );
	}else if( typeof obj == "boolean" ) {	
		ret = i3xx.BooleanToJson( obj, w );
	}else if( typeof obj == "object" && obj instanceof Date ) {	
		ret = i3xx.DateToJson( obj, w );
	}else if( typeof obj == "number" ) {	
		ret = i3xx.NumberToJson( obj, w );
	}else if( typeof obj == "object" ) {	
		ret = i3xx.ObjectToJson( obj, w );
	}else if( typeof obj == "string" ) {	
		ret = i3xx.StringToJson( obj, w );
	}
	return ret;
}); 

/**
 * i3xx.ArrayToJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:57:45
 */
i3xx.define( "i3xx.ArrayToJson", function( obj, w ){  
        var a = [],     // The array holding the partial texts.
            i,          // Loop counter.
            l = obj.length,
            v;          // The value to be stringified.

// For each value in this array...

        //for (i = 0; i < l; i += 1) {
        for (i in obj) {
            v = obj[i];
            switch (typeof v) {
            case 'object':

// Serialize a JavaScript object value. Ignore objects thats lack the
// toJSONString method. Due to a specification error in ECMAScript,
// typeof null is 'object', so watch out for that case.

                if (v) {
                    if (typeof i3xx.toJson === 'function') {
                        a.push( i3xx.toJson( v, w ) );
                    }
                } else {
                    a.push('null');
                }
                break;

            case 'string':
            case 'number':
            case 'boolean':
                a.push(i3xx.toJson( v ) );

// Values without a JSON representation are ignored.

            }
        }

// Join all of the member texts together and wrap them in brackets.

        return '[' + a.join(',') + ']';
} );

/**
 * i3xx.BooleanToJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:57:45
 */
i3xx.define( "i3xx.BooleanToJson", function( obj ){  
	return String(obj);
} );
/*
 * Schalter bestimmt, ob ein Datum als String �bertragen wird oder als Date Objekt
 */
i3xx.define( "i3xx.json.DATE_AS_STRING", false);

/**
 * i3xx.DateToJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:57:45
 */
i3xx.define( "i3xx.DateToJson", function( obj ){  
//Datum als Date Objekt �bertragen

// Eventually, this method will be based on the date.toISOString method.

        function f(n) {

// Format integers to have at least two digits.

            return n < 10 ? '0' + n : n;
        }

		if( i3xx.json.DATE_AS_STRING ){

        return '"' + obj.getFullYear() + '-' +
                f(obj.getMonth() + 1)  + '-' +
                f(obj.getDate())       + 'T' +
                f(obj.getHours())      + ':' +
                f(obj.getMinutes())    + ':' +
                f(obj.getSeconds())    + 'Z"';
		}else{
			return 'new Date( '+obj.getTime()+' )';
		}
 } );
/**
 * i3xx.NumberToJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:57:45
 */
i3xx.define( "i3xx.NumberToJson", function( obj, w ){  

// JSON numbers must be finite. Encode non-finite numbers as null.

        return isFinite(obj) ? String(obj) : 'null';
} );
/**
 * i3xx.ObjectToJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:57:45
 */
i3xx.define( "i3xx.ObjectToJson", function( obj, w ){  
        var a = [],     // The array holding the partial texts.
            k,          // The current key.
            i,          // The loop counter.
            v;          // The current value.

// If a whitelist (array of keys) is provided, use it assemble the components
// of the object.
        if (w) {
            for (i = 0; i < w.length; i += 1) {
                k = w[i];
                if (typeof k === 'string') {
                    v = obj[k];
                    switch (typeof v) {                
                    case 'object':

// Serialize a JavaScript object value. Ignore objects that lack the
// toJSONString method. Due to a specification error in ECMAScript,
// typeof null is 'object', so watch out for that case.

                        if (v) {
                            if (typeof i3xx.toJson === 'function') {
                                a.push(i3xx.toJson( k ) + ':' +
                                       i3xx.toJson( v, w ) );
                            }
                        } else {
                            a.push( i3xx.toJson( k ) + ':null');
                        }
                        break;

                    case 'string':
                    case 'number':
                    case 'boolean':
                        a.push( i3xx.toJson( k ) + ':' + i3xx.toJson( v ));

// Values without a JSON representation are ignored.

                    }
                }
            }
        } else {

// Iterate through all of the keys in the object, ignoring the proto chain
// and keys that are not strings.

            for (k in obj) {
                if (typeof k === 'string' &&
                        Object.prototype.hasOwnProperty.apply(obj, [k])) {
                    v = obj[k];
                    switch (typeof v) {
                    case 'object':

// Serialize a JavaScript object value. Ignore objects that lack the
// toJSONString method. Due to a specification error in ECMAScript,
// typeof null is 'object', so watch out for that case.

                        if (v) {
                            if (typeof i3xx.toJson === 'function') {
                                a.push(i3xx.toJson( k ) + ':' +
                                       i3xx.toJson( v ));
                            }
                        } else {
                            a.push( i3xx.toJson( k ) + ':null');
                        }
                        break;

                    case 'string':
                    case 'number':
                    case 'boolean':
                        a.push( i3xx.toJson( k ) + ':' + i3xx.toJson( v ) );

// Values without a JSON representation are ignored.

                    }
                }
            }
        }

// Join all of the member texts together and wrap them in braces.

        return '{' + a.join(',') + '}';
} );
/**
 * i3xx.StringToJson
 * Author: 	innovative dialogsysteme GmbH,  BBoers
 * Created:	30.08.2007, 15:57:45
 */
i3xx.define( "i3xx.StringToJson", function( obj ){  
	var m = {
	            '\b': '\\b',
	            '\t': '\\t',
	            '\n': '\\n',
	            '\f': '\\f',
	            '\r': '\\r',
	            '"' : '\\"',
	            '\\': '\\\\'
	        };
// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can simply slap some quotes around it.
// Otherwise we must also replace the offending characters with safe
// sequences.

            if (/["\\\x00-\x1f]/.test(obj)) {
                return '"' + obj.replace(/[\x00-\x1f\\"]/g, function (a) {
                    var c = m[a];
                    if (c) {
                        return c;
                    }
                    c = a.charCodeAt();
                    return '\\u00' +
                        Math.floor(c / 16).toString(16) +
                        (c % 16).toString(16);
                }) + '"';
            }
            return '"' + obj + '"';
} );
 
/*
    json.js
    2007-08-19

    Public Domain

    This file adds these methods to JavaScript:

        array.toJSONString(whitelist)
        boolean.toJSONString()
        date.toJSONString()
        number.toJSONString()
        object.toJSONString(whitelist)
        string.toJSONString()
            These methods produce a JSON text from a JavaScript value.
            It must not contain any cyclical references. Illegal values
            will be excluded.

            The default conversion for dates is to an ISO string. You can
            add a toJSONString method to any date object to get a different
            representation.

            The object and array methods can take an optional whitelist
            argument. A whitelist is an array of strings. If it is provided,
            keys in objects not found in the whitelist are excluded.

        string.parseJSON(filter)
            This method parses a JSON text to produce an object or
            array. It can throw a SyntaxError exception.

            The optional filter parameter is a function which can filter and
            transform the results. It receives each of the keys and values, and
            its return value is used instead of the original value. If it
            returns what it received, then structure is not modified. If it
            returns undefined then the member is deleted.

            Example:

            // Parse the text. If a key contains the string 'date' then
            // convert the value to a date.

            myData = text.parseJSON(function (key, value) {
                return key.indexOf('date') >= 0 ? new Date(value) : value;
            });

    It is expected that these methods will formally become part of the
    JavaScript Programming Language in the Fourth Edition of the
    ECMAScript standard in 2008.

    This file will break programs with improper for..in loops. See
    http://yuiblog.com/blog/2006/09/26/for-in-intrigue/

    This is a reference implementation. You are free to copy, modify, or
    redistribute.

    Use your own copy. It is extremely unwise to load untrusted third party
    code into your pages.
*/

//Konvertierung XPath JSON Path
i3xx.define("i3xx.jpToXPath", function( str ){
	str = str.replace(/^$\./i,".");
	str = str.replace(/\./gi,"/");
	return str;
});
i3xx.define("i3xx.xpathToJP", function( str ){
	str = str.replace(/^\//i,"$.");
	//str = str.replace(/\[\.\///gi,"[?(@.");
	str = str.replace(/\[\./gi,"[?(@");
	str = str.replace(/\]/gi,")]");
	var result = str.match(/\[([0-9]+)\)\]/gi);
	if (result)
  		for (var i = 0; i < result.length; ++i){
			str = str.replace(/\[([0-9]+)\)\]/i,"["+(parseInt(result[i].replace(/\D/gi,""))-1)+"]");	
		}

	//str = str.replace(/\[([0-9]+)\)\]/gi,"[$1]");
	//alert("test"+RegExp.$1);
	str = str.replace(/=/gi,"==");
	str = str.replace(/\//gi,".");
	return str;
});


/* JSONPath 0.8.0 - XPath for JSON
 *
 * Copyright (c) 2007 Stefan Goessner (goessner.net)
 * Licensed under the MIT (MIT-LICENSE.txt) licence.
 Upgrades by Kris Zyp, SitePen:
 Fixed bugs with special characters in the object keys, and operator keys
 Fixed bug with nested [([()])]
 Add result based evaluation argument
 evalType=="RESULT" will use result based evaluation instead of ITEM based (the default)
 I switched the code from recursive to iterative in order implement the changes

Operator 		Description 				Example 				Results
$ 				The root node 				$ 						The whole document
. or [] 		The child member or element $.store.book 			The contents of the book member of the store object
.. 				Recursive descent 			$..author 				The content of the author member in every object
* 				Wildcard 					$.store.book[*].author 	The content of the author member of any object of the array contained in the book member of the store object
[] 				Subscript 					$.store.book[0] 		The first element of the array contained in the book member of the store object
[,] 			Set 						$.store.book[0,1] 		The first two elements of the array contained in the book member of the store object
[start:end:step]Slice 						$.store.book[:2] 		The first two elements of the array contained in the book member of the store object; the start and step are omitted and implied to be 0 and 1, respectively
 */
i3xx.define( "i3xx.jsonPath", function (obj, expr, arg) {
   var P = {
      resultType: arg && arg.resultType || "VALUE",
      result: [],
      normalize: function(expr) {
         var subx = [];
         return expr.replace(/[\['](\??\(.*?\))[\]']/g, function($0,$1){return "[#"+(subx.push($1)-1)+"]";})
                    .replace(/'?\.'?|\['?/g, ";")
                    .replace(/;;;|;;/g, ";..;")
                    .replace(/;$|'?\]|'$/g, "")
                    .replace(/#([0-9]+)/g, function($0,$1){return subx[$1];});
      },
      asPath: function(path) {
         var x = path.split(";"), p = "$";
         for (var i=1,n=x.length; i<n; i++)
            p += /^[0-9*]+$/.test(x[i]) ? ("["+x[i]+"]") : ("['"+x[i]+"']");
         return p;
      },
      store: function(p, v) {
         if (p) P.result[P.result.length] = P.resultType == "PATH" ? P.asPath(p) : v;
         return !!p;
      },
      trace: function(expr, val, path) {
         if (expr) {
            var x = expr.split(";"), loc = x.shift();
            x = x.join(";");
            if (val && val.hasOwnProperty(loc))
               P.trace(x, val[loc], path + ";" + loc);
            else if (loc === "*")
               P.walk(loc, x, val, path, function(m,l,x,v,p) { P.trace(m+";"+x,v,p); });
            else if (loc === "..") {
               P.trace(x, val, path);
               P.walk(loc, x, val, path, function(m,l,x,v,p) { typeof v[m] === "object" && P.trace("..;"+x,v[m],p+";"+m); });
            }
            else if (/,/.test(loc)) { // [name1,name2,...]
               for (var s=loc.split(/'?,'?/),i=0,n=s.length; i<n; i++)
                  P.trace(s[i]+";"+x, val, path);
            }
            else if (/^\(.*?\)$/.test(loc)) // [(expr)]
               P.trace(P.eval(loc, val, path.substr(path.lastIndexOf(";")+1))+";"+x, val, path);
            else if (/^\?\(.*?\)$/.test(loc)) // [?(expr)]
               P.walk(loc, x, val, path, function(m,l,x,v,p) { if (P.eval(l.replace(/^\?\((.*?)\)$/,"$1"),v[m],m)) P.trace(m+";"+x,v,p); });
            else if (/^(-?[0-9]*):(-?[0-9]*):?([0-9]*)$/.test(loc)) // [start:end:step]  phyton slice syntax
               P.slice(loc, x, val, path);
         }
         else
            P.store(path, val);
      },
      walk: function(loc, expr, val, path, f) {
         if (val instanceof Array) {
            for (var i=0,n=val.length; i<n; i++)
               if (i in val)
                  f(i,loc,expr,val,path);
         }
         else if (typeof val === "object") {
            for (var m in val)
               if (val.hasOwnProperty(m))
                  f(m,loc,expr,val,path);
         }
      },
      slice: function(loc, expr, val, path) {
         if (val instanceof Array) {
            var len=val.length, start=0, end=len, step=1;
            loc.replace(/^(-?[0-9]*):(-?[0-9]*):?(-?[0-9]*)$/g, function($0,$1,$2,$3){start=parseInt($1||start);end=parseInt($2||end);step=parseInt($3||step);});
            start = (start < 0) ? Math.max(0,start+len) : Math.min(len,start);
            end   = (end < 0)   ? Math.max(0,end+len)   : Math.min(len,end);
            for (var i=start; i<end; i+=step)
               P.trace(i+";"+expr, val, path);
         }
      },
      eval: function(x, _v, _vname) {
         try { return $ && _v && eval(x.replace(/@/g, "_v")); }
         catch(e) { throw new SyntaxError("jsonPath: " + e.message + ": " + x.replace(/@/g, "_v").replace(/\^/g, "_a")); }
      }
   };

   var $ = obj;
   if (expr && obj && (P.resultType == "VALUE" || P.resultType == "PATH")) {
      P.trace(P.normalize(expr).replace(/^\$;/,""), obj, "$");
      return P.result.length ? P.result : false;
   }
}); 

/*	This work is licensed under Creative Commons GNU LGPL License.

	License: http://creativecommons.org/licenses/LGPL/2.1/
   Version: 0.9
	Author:  Stefan Goessner/2006
	Web:     http://goessner.net/ 
*/
i3xx.define("i3xx.json2xml", function json2xml(o, tab) {
   var toXml = function(v, name, ind) {
      var xml = "";
      if (v instanceof Array) {
         for (var i=0, n=v.length; i<n; i++)
            xml += ind + toXml(v[i], name, ind+"\t") + "\n";
      }
      else if (typeof(v) == "object") {
         var hasChild = false;
         xml += ind + "<" + name;
         for (var m in v) {
            if (m.charAt(0) == "@")
               xml += " " + m.substr(1) + "=\"" + v[m].toString() + "\"";
            else
               hasChild = true;
         }
         xml += hasChild ? ">" : "/>";
         if (hasChild) {
            for (var m in v) {
               if (m == "#text")
                  xml += v[m];
               else if (m == "#cdata")
                  xml += "<![CDATA[" + v[m] + "]]>";
               else if (m.charAt(0) != "@")
                  xml += toXml(v[m], m, ind+"\t");
            }
            xml += (xml.charAt(xml.length-1)=="\n"?ind:"") + "</" + name + ">";
         }
      }
      else {
         xml += ind + "<" + name + ">" + v.toString() +  "</" + name + ">";
      }
      return xml;
   }, xml="";
   for (var m in o)
      xml += toXml(o[m], m, "");
   return tab ? xml.replace(/\t/g, tab) : xml.replace(/\t|\n/g, "");
});

/*
xml2json v 1.1
copyright 2005-2007 Thomas Frank

This program is free software under the terms of the 
GNU General Public License version 2 as published by the Free 
Software Foundation. It is distributed without any warranty.
*/
i3xx.define("i3xx.Xml2JsonParser", {
	constructor	: function(){},
	parser:function(xmlcode,ignoretags,debug){
		if(!ignoretags){ignoretags="";}
		xmlcode=xmlcode.replace(/\s*\/>/g,'/>');
		xmlcode=xmlcode.replace(/<\?[^>]*>/g,"").replace(/<\![^>]*>/g,"");
		if (!ignoretags.sort){ignoretags=ignoretags.split(",");}
		var x=this.no_fast_endings(xmlcode);
		x=this.attris_to_tags(x);
		x=escape(x);
		x=x.split("%3C").join("<").split("%3E").join(">").split("%3D").join("=").split("%22").join("\"");
		for (var i=0;i<ignoretags.length;i++){
			x=x.replace(new RegExp("<"+ignoretags[i]+">","g"),"*$**"+ignoretags[i]+"**$*");
			x=x.replace(new RegExp("</"+ignoretags[i]+">","g"),"*$***"+ignoretags[i]+"**$*");
		}
		x='<jsontagwrapper>'+x+'</jsontagwrapper>';		
		this.xmlobject={};
		var y=this.xml_to_object(x).jsontagwrapper;
		if(debug){y=this.show_json_structure(y,debug);}
		return y;
	},
	xml_to_object:function(xmlcode){
		//alert(xmlcode);
		var x=xmlcode.replace(/<\//g,"�");
		x=x.split("<");
		var y=[];
		var level=0;
		var opentags=[];
		for (var i=1;i<x.length;i++){
			var tagname=x[i].split(">")[0];
			opentags.push(tagname);
			level++;
			y.push(level+"<"+x[i].split("�")[0]);
			while(x[i].indexOf("�"+opentags[opentags.length-1]+">")>=0){level--;opentags.pop();}
		}
		//alert(y.join("\n"));
		var oldniva=-1;
		var objname="this.xmlobject";
		for (var i=0;i<y.length;i++){
			var preeval="";
			var niva=y[i].split("<")[0];
			var tagnamn=y[i].split("<")[1].split(">")[0];
			//Tagnamen so umsetzen, wie im XML, BB 20081111
			//tagnamn=tagnamn.toLowerCase();
			//tagnamn="\""+tagnamn+"\"";
			var rest=y[i].split(">")[1];
			//alert(y[i]+"\n"+niva+"<="+oldniva);
			if(niva<=oldniva){
				var tabort=oldniva-niva+1;
				//alert(tabort+":"+objname);
				for (var j=0;j<tabort;j++){
					objname=objname.substring(0,this.getBorder(objname,'['));
				}
			}
			objname+="."+tagnamn;
			var pobject=objname.substring(0,this.getBorder(objname,']'));
			//alert("po:"+pobject+":\n"+eval("typeof "+pobject));
			if (eval("typeof "+pobject) != "object"){preeval+=pobject+"={value:"+pobject+"};\n";}
			var objlast=objname.substring(this.getBorder(objname, '[', true));
			objlast = objlast.replace(/[\[\"\]]/gi,"");
			var already=false;
			for (k in eval(pobject)){if(k==objlast){already=true;}}
			var onlywhites=true;
			for(var s=0;s<rest.length;s+=3){
				if(rest.charAt(s)!="%"){onlywhites=false;}
			}
			if (rest!="" && !onlywhites){
				if(rest/1!=rest){
					rest="'"+rest.replace(/\'/g,"\\'")+"'";
					rest=rest.replace(/\*\$\*\*\*/g,"</");
					rest=rest.replace(/\*\$\*\*/g,"<");
					rest=rest.replace(/\*\*\$\*/g,">");
				}
			} 
			else {rest="{}";}
			if(rest.charAt(0)=="'"){rest='unescape('+rest+')';}
			if (already && !eval(objname+".sort")){preeval+=objname+"=["+objname+"];\n";}
			var before="=";after="";
			if (already){before=".push(";after=")";}
			//alert(preeval+":"+objname+":"+before+":"+rest+":"+after);
			if(objname.indexOf(".@") > -1 ){
				objname = objname.replace("\.@", "[\"@")+"\"]";
				//alert(objname);
			}
			var toeval=preeval+objname+before+rest+after;
			//alert(toeval);
			eval(toeval);
			if(eval(objname+".sort")){objname+="["+eval(objname+".length-1")+"]";}
			oldniva=niva;
		}
		return this.xmlobject;
	},
	getBorder	: function( str, c, spec ){
		var pos = str.lastIndexOf(".");
		var pos1 = str.lastIndexOf(c); 
		if (pos1 > pos) {
			pos = pos1;
		}else if (spec) {
			pos = pos + 1;
		}
		return pos;
	},
	
	show_json_structure:function(obj,debug,l){
		var x='';
		if (obj.sort){x+="[\n";} else {x+="{\n";}
		for (var i in obj){
			if (!obj.sort){x+="\""+i+"\":";}
			if (typeof obj[i] == "object"){
				x+=this.show_json_structure(obj[i],false,1);
			}
			else {
				if(typeof obj[i]=="function"){
					var v=obj[i]+"";
					//v=v.replace(/\t/g,"");
					x+=v;
				}
				else if(typeof obj[i]!="string"){x+=obj[i]+",\n";}
				else {x+="'"+obj[i].replace(/\'/g,"\\'").replace(/\n/g,"\\n").replace(/\t/g,"\\t").replace(/\r/g,"\\r")+"',\n";}
			}
		}
		if (obj.sort){x+="],\n";} else {x+="},\n";}
		if (!l){
			x=x.substring(0,x.lastIndexOf(","));
			x=x.replace(new RegExp(",\n}","g"),"\n}");
			x=x.replace(new RegExp(",\n]","g"),"\n]");
			var y=x.split("\n");x="";
			var lvl=0;
			for (var i=0;i<y.length;i++){
				if(y[i].indexOf("}")>=0 || y[i].indexOf("]")>=0){lvl--;}
				tabs="";for(var j=0;j<lvl;j++){tabs+="\t";}
				x+=tabs+y[i]+"\n";
				if(y[i].indexOf("{")>=0 || y[i].indexOf("[")>=0){lvl++;}
			}
			if(debug=="html"){
				x=x.replace(/</g,"&lt;").replace(/>/g,"&gt;");
				x=x.replace(/\n/g,"<BR>").replace(/\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;");
			}
			if (debug=="compact"){x=x.replace(/\n/g,"").replace(/\t/g,"");}
		}
		return x;
	},
	no_fast_endings:function(x){
		x=x.split("/>");
		for (var i=1;i<x.length;i++){
			var t=x[i-1].substring(x[i-1].lastIndexOf("<")+1).split(" ")[0];
			x[i]="></"+t+">"+x[i];
		}
		x=x.join("");
		return x;
	},
	attris_to_tags: function(x){
		var d=' ="\''.split("");
		x=x.split(">");
		for (var i=0;i<x.length;i++){
			var temp=x[i].split("<");
			for (var r=0;r<4;r++){temp[0]=temp[0].replace(new RegExp(d[r],"g"),"_jsonconvtemp"+r+"_");}
			if(temp[1]){
				temp[1]=temp[1].replace(/'/g,'"');
				temp[1]=temp[1].split('"');
				for (var j=1;j<temp[1].length;j+=2){
					for (var r=0;r<4;r++){temp[1][j]=temp[1][j].replace(new RegExp(d[r],"g"),"_jsonconvtemp"+r+"_");}
				}
				temp[1]=temp[1].join('"');
			}
			x[i]=temp.join("<");
		}
		x=x.join(">");
		x=x.replace(/ ([^=]*)=([^ |>]*)/g,"><@$1>$2</@$1");
		x=x.replace(/>"/g,">").replace(/"</g,"<");
		for (var r=0;r<4;r++){x=x.replace(new RegExp("_jsonconvtemp"+r+"_","g"),d[r]);}
		return x;
	}
});

i3xx.define("i3xx.xml2jsonA", function( xmlcode, ignoretags,debug){
	return (new i3xx.Xml2JsonParser()).parser(xmlcode, ignoretags, debug);
});

i3xx.define("i3xx.xml2json", function( xml ){
	var ret = xml;
	//XML Deklaration entfernen
	ret = ret.replace(/^<\?.*\?>/,"");
	//Tabs entfernen
	ret = ret.replace(/^[\t\s]*</g, "<");
	//Zeilenumbruch umsetzen
	ret = ret.replace(/>\s*</g, ">\n<");
	//Endtags umsetzen
	ret = ret.replace(/<\/\w+>/g, "},");
	//out.println("1:\n"+ret);
	//leere geschlossene Tags umsetzen
	ret = ret.replace(/\s*\/\>/g, ">},");
	//out.println("1:a\n"+ret);
	ret = ret.replace(/>/g, "\":{");
	//out.println("2:\n"+ret);
	ret = ret.replace(/</g, "\"");
	//out.println("3:\n"+ret);
	//Textknoten sch�tzen
	ret = ret.replace(/\"\:\{(.*)\},/g, "\":{\"$1\"},");
	//out.println("4:\n"+ret);
	//Textknoten von Knoten mit Attributen umsetzen
	ret = ret.replace(/\"\:\{\"(.*)\"\},/g, "\":{\n\"#text\":\"$1\"},");
	//out.println("5:\n"+ret);
	ret = ret.replace(/\s\w+=\".*\"\"\:\{/g, function($0, $1, $2){
		//out.println("$0:"+$0);out.println("$1:"+$1);out.println("$2:"+$2);		
		var str = "";
		var tmp = ""+$0;
		//Blank vorne und "":{ entfernen
		tmp = tmp.substring(1,tmp.length-4);
		var arr = tmp.split("\" ");
		for( var i in arr ){
			var arr2 = arr[i].split("=\"");
			if( arr2.length == 2 )
				str += "\n\"@"+arr2[0]+"\":"+"\""+arr2[1]+"\",";
		}
		return "\":{"+str;});
	//out.println("6:\n"+ret);

	//Ojekte mit reinen Textknoten umsetzen
	ret = ret.replace(/\"\:\{\n\"#text\"\:\"(.*)\"\},/g, "\":\"$1\",");
	//out.println("7:\n"+ret);

/*
"a":{
"b":{
"@c":"1",
"@c2":"TEST",
"d":"",
"d":"",
"d":"f",
"d":{
"@g":"hello:{",
"#text":"world"},
"d":"magic mushroom",
"d":{
"@c":"1",
"@c2":"TEST",
"h":"fan",
"h":{
"@test":"tast",
"#text":"ic"},
"h":"converter",
},
},
}, 
*/
	/**
	 * f�gt ein Objekt mit dem Namen name in obj ein
	 * ist das Objekt bereits vorhanden , wir ein Array erzeugt
	 * @param {Object} obj
	 * @param {Object} name
	 * @param {Object} value
	 */
	function setObject(obj, name, value ){
		//out.println("set "+obj+", "+name+":"+value);
		var ret = obj;
		//wenn obj ein Array ist
		var tmp = null;
		if (typeof obj.sort == "function") {
			for( var i in obj ){
				if( typeof obj[i]=="object" ){
					//Suchen, ob im Array ein Objekt mit diesem Feld existiert
					if( obj[i][name] != null){
						tmp = obj[i];
						break;
					}
				}
			}
			//Objekt im Array anlegen
			if( tmp == null ){
				obj[obj.length] = {};
				tmp = obj[obj.length-1];
			}
		}
		if( tmp != null ){
			//mit dem Array weiterarbeiten
			obj = tmp;
		}
		
		if( obj[name] == null ){
			//wenn noch nicht gesetzt
			obj[name] = value;
			ret = obj[name];
		}else if( typeof obj[name].sort == "function"){
			//wenn bereits Array
			obj[name][obj[name].length] = value;
			if(typeof value == "object")
				ret = value;
			else
				ret = obj[name];
		}else{
			//wenn Objekt in Array umwandeln
			var old = obj[name];
			obj[name] = [old,value];
			if(typeof value == "object")
				ret = value;
			else	
				ret = obj[name];
		}
		return ret;
	}
	
	function addObject( str, obj, par, pidx ){
		if( par == null ){
			par = [];
			pidx = -1;			
		}
		var line = str.substring(0,str.indexOf("\n"));
		if( line == "" )
			line = str;
		//out.println(pidx+"."+par.length+", json:\n"+i3xx.toJson(obj));
		//out.println("parse line "+line+ ":"+(line == "},"));
		if( line == "" )
			return;
		var rest = str.substr(str.indexOf("\n")+1);
		if( rest.indexOf("\n") < 0 )
			rest = "";
		//Ende der Ebene
		if(line == "},"){
			//schliessen des Objekts
			//Eine Ebene h�her
			pidx--;
			addObject(rest, par[pidx], par, pidx );
			return;
		}
		
		var re = /^\"(.*)\"\:(.*)$/;
		var arr = re.exec( line );
		/*for(var i in arr ){
			out.println(i+":"+arr[i]);
		}*/
		if( arr[2] == "{" ){
			//Neues Objekt erzeugen
			var sret = setObject(obj, arr[1], {} );
			par[par.length] = obj[arr[1]];
			
			//eine Ebene tiefer
			pidx++;
			addObject(rest, sret, par, pidx );
		}else if(arr[2].charAt(0) == "\""){
			//Textknoten
			var val = arr[2].substring(1,arr[2].lastIndexOf("\""));
			setObject(obj, arr[1], val );
			var rxp = /\"\},$/;
			//pr�fen, ob Objekt geschlossen wird
			if( rxp.test( arr[2] ) ){
				//Eine Ebene h�her
				pidx--;
				//out.println(pidx+":"+i3xx.toJson(par[pidx])+", "+obj+", "+line);
				addObject(rest, par[pidx], par, pidx );
			}else{
				//auf der selben Ebene bleiben
				addObject(rest, obj, par, pidx );				
			}
		}
		return;
	}

	//f�hrende Zeilenumbr�che entfernen
	ret = ret.replace(/^\n*/, "" );

	//wenn doppelte Namen vorliegen, diese als Array zusammenfassen
	var res = {};
	addObject(ret,res);		
	//out.println("test: \n"+i3xx.toJson(res));
	
	//ret = "{"+ret+"}";
			
	return res;
});

/*if(!Array.prototype.push){
	Array.prototype.push=function(x){
		this[this.length]=x;
		return true
	}
};

if (!Array.prototype.pop){
	Array.prototype.pop=function(){
  		var response = this[this.length-1];
  		this.length--;
  		return response
	}
};
*/
