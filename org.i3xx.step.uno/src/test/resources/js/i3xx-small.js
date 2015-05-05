/** 
* @projectDescription 	OfficeBase Scriptbibliothek small version
*
* @author	Benny Boers benny.boers@v-d-v.de
* @version	0.2 
* @namespace i3xx
* @feature = i3xx.i3xx
*/

if((typeof i3xx) === "undefined" ){
	//defines base i3xx package
	i3xx = {};
	/**
	 *  Kontextobjekt im Server Ausf�hrumgebung im Client Window
	 *  @id i3xx.context 
	 */
	i3xx.context = this;
	
	/**
	 * pr�ft, ob der �bergebene Wert ein regul�rer Ausdruck ist
	 * 
	 * @param {Object} exp	Ausdruck
	 */
	i3xx.isRegExp = function ( exp ){	
		var sTypeOf = typeof exp;
		if( sTypeOf == "function" )
		{
			var func = exp.toString();
			if( ( /^\/.*\/$/ ).test( func ) ){
				return true;
			}
		}else if(sTypeOf == "object" ){
			var fconst = exp.constructor;
		    if( fconst != null )
			{
				if( fconst == RegExp )
					return true;
			}	
		}
		
		return false;
	};
	
	i3xx.isProto = function ( ){	
		if( Object.__proto__ != null ){
			return true;
		}
		return false;
	};
	
	/**
	 * Z�hler zum eindeutigen definieren der tempor�ren Superfunktion
	 * @private
	 * @property {Number}
	 */ 
	i3xx._SUPERFUNCNAME_IDX = -1;
	
	/**
	 * pr�ft ob eine Variable belegt ist
	 * @id i3xx.isDefined
	 * 
	 * @param {String} path	ist der Variablenpfad z.B. "i3xx.event.OB4EventSystem"
	 * @param {boolean} nullAllowed	optional gibt an ob null Definition als definiert gilt default false
	 * @param {Object} win	optional f�r ein bestimmtes Fenster oder Objektpfad in dem auf vorhandensein gepr�ft werden soll
	 * @return {Boolean} true, wenn ein Pfad definiert ist
	 */
	i3xx.isDefined = function( path, nullAllowed, win ){
		if( nullAllowed == null )
			nullAllowed = false;
		
		if( win == null )
			win = i3xx.context;
		
		var arr = path.split(".");
		var tmp = win;
		var ret = true;
		try{			
			for( var i in arr ){
				//if( tmp[arr[i]] === undefined ){
				if( (typeof tmp[arr[i]] === 'undefined')){
					ret = false;
					break;
				}else if( !nullAllowed && tmp[arr[i]] === null ){
					ret = false;
					break;
				}
				tmp = tmp[arr[i]];
			}
		}catch(e){
			//bei der Abfrage in einem anderen Fenster kann eine Permission denied Exception auftreten
			//in diesem gibt die Methode false zur�ck, damit kein Abbruch erfolgt
			console.log(e);
			ret = false;
		}
	
		return ret;
	};
	
	/**
	 * defines a package or object and creates empty object object can 
	 * extend another definition Beispiel zu Objekterweiterung in JavaScript: 
	 * var B = {test2:"hello B",t:function(){return this.test2}}; 
	 * var A = function(){}; 
	 * A.prototype = {test:"hello",test2:"hello A"}; 
	 * A.prototype.__proto__ = B; 
	 * var a = new A(); 
	 * a.test2+"-"+a.__proto__.__proto__.t()+"-";
	 * 
	 * you can assign an Object to a pkg path if the objectdefinition has no constructor
	 * so you have to create an object with an instance method like a singleton
	 * 
	 * @alias i3xx.define
	 * @id i3xx.define
	 * @param {String} pkg	Paketname
	 * @param {Object} def	Objektdefninition
	 * @param {Object} ext	Objekterweiterung
	 */
	i3xx.define = function( pkg, def, ext ){
		if( pkg == null || pkg == "" )
			return;
		//jedes PKG kann nur einmal definiert werden
		if( i3xx.isDefined( pkg, false ) )
			return;		
		if( def == null )
			def = {};
				
		var arr = pkg.split(".");
		var root = this.context;	//this bietet Zugriff auf globalen Context
		for( var i in arr ){
			if( arr[i] == null || arr[i] == "" )
				continue;
	
			//alert(i+"\n"+root+"\n"+arr[i]+"\n"+root[arr[i]]);
			if( root[arr[i]] == null ){
				if( i == arr.length-1 ){
					if( typeof def == "function" || typeof def == "string" || typeof def == "number"  || 
						typeof def == "boolean" || (def instanceof Array) || i3xx.isRegExp(def) ){
						root[arr[i]] = def;
					}else{
						//Namen mit Paketangabe als _pkgname Eigenschaft setzen
						if( def._pkgname == null ){
							def._pkgname = pkg;
						}	
						//Object definition
						//pr�fen, ob Singleton
						if( def.constructor == null || (""+def.constructor).indexOf("function Object()")==0 ){
							root[arr[i]] = def;										
						}else{
							root[arr[i]]=def.constructor;
							//var cl = new i3xx.clone(def);
							//cl.prototype = def;
							root[arr[i]].prototype=def;			
						}
											
						//Objekt die Methoden verpassen
						if( ext != null ){							
							if( typeof ext == "object" ){
								if( root[arr[i]].prototype == null ){
									//var cl = new i3xx.clone(ext);
									//cl.prototype = ext;
									root[arr[i]].prototype = ext;					
								}else{

									//var testobj = root[arr[i]].prototype;
									var testobj = root[arr[i]].prototype;
									
									root[arr[i]].prototype["superclass"] = ext;					

									//IE,Opera kennt Vererbung �ber __proto__ nicht
									var xobj =  root[arr[i]].prototype["superclass"];
									for( var j in xobj ){											
										if( typeof root[arr[i]].prototype[j] == "undefined" ){
											root[arr[i]].prototype[j] = xobj[j];
										}
									}
									
								}							
							}else{ //Function
								if( root[arr[i]].prototype == null ){
									//var cl = new i3xx.clone(ext);
									//cl.prototype = ext;
									var nobj = new ext();
									root[arr[i]].prototype = nobj;
									if( i3xx.isProto() ){
										root[arr[i]].prototype["superclass"] = nobj.__proto__; 												
									}else{
										root[arr[i]].prototype["superclass"] = nobj.prototype; 								
									}
								}else{
									//IM IE, wenn __proto__ nicht bekannt ist __proto__ == null, sonst [object]									
									root[arr[i]].prototype.__proto__ = new ext();
									root[arr[i]].prototype["superclass"] = root[arr[i]].prototype.__proto__;	
									//IE,Opera kennt Vererbung �ber __proto__ nicht
									if( !i3xx.isProto() ){
										var xobj =  root[arr[i]].prototype.__proto__;
										for( var j in xobj ){
											if( typeof root[arr[i]].prototype[j] == "undefined" ){
												root[arr[i]].prototype[j] = xobj[j];
											}
										}
									}				
								}							
							}
						}
					}
				}else{
					//just a Package
					root[arr[i]]={};
				}
			}
			root = root[arr[i]];
		}

	};
		
	i3xx.define( "i3xx.feature", function(/*String*/ filename){
	});
	
	i3xx.define( "i3xx.require", function(/*String*/ filename){
	});
	
	i3xx.define( "i3xx.isNumber", function(/*anything*/ it){
		if( it == null ){
			return false;
		}
		// summary:	Return true if it is a Number
		return typeof it == "number" || it instanceof Number; // Boolean
	});
	
	i3xx.define( "i3xx.isString", function(/*anything*/ it){
		if( it == null ){
			return false;
		}
		// summary:	Return true if it is a String
		return typeof it == "string" || it instanceof String; // Boolean
	});
	
	i3xx.define( "i3xx.isFile", function(/*anything*/ it){
		if( it == null ){
			return false;
		}
		// summary: Return true if it is a File
		return (it && it instanceof File); // Boolean
	});

	i3xx.define( "i3xx.isArray", function(/*anything*/ it){
		if( it == null ){
			return false;
		}
		// summary: Return true if it is an Array
		return (it && it instanceof Array || typeof it == "array"); // Boolean
	});

	i3xx.define( "i3xx.isArrayLike", function(/*anything*/ it){
		return it && it !== undefined && // Boolean
			// keep out built-in constructors (Number, String, ...) which have length
			// properties
			!i3xx.isString(it) && !i3xx.isFunction(it) &&
			!(it.tagName && it.tagName.toLowerCase() == 'form') &&
			(i3xx.isArray(it) || isFinite(it.length));
	});
	
	i3xx.define( "i3xx.isFunction", function(/*anything*/it){
		if( it == null ){
			return false;
		}
		return (typeof it == "function" || it instanceof Function); // Boolean
	});
	
	i3xx.define( "i3xx.isObject", function(/*anything*/ it){
		if( it == null ){
			return false;
		}
		// summary: 
		//		Returns true if it is a JavaScript object (or an Array, a Function or null)
		return (it !== undefined &&
			(it === null || typeof it == "object" || i3xx.isArray(it) || i3xx.isFunction(it))); // Boolean
	});
	
	i3xx.define( "i3xx.toObject", function(/*anything*/ it){
		// summary:	Return a simple type as String or Number
		return (typeof it == "number") ? new Number(it): (typeof it == "string") ? new String(it) : it;
	});
	
	i3xx.define( "i3xx.classof", function(/*anything*/ it, /*String*/ type){
		// summary:	Test the _classname , use classname() because of the hostobject
		return (typeof it == "object") ? it.classname() ? it.classname() == type : false : false;
	});
	
	i3xx.define( "i3xx.typeof", function(/*anything*/ it, /*String*/ type){
		// summary:	Test the _pkgname
		return (typeof it == "object") ? it._pkgname ? it._pkgname == type : false : false;
	});
	
	
	
	
	
	i3xx.define("i3xx.instance", function(/*String*/name,/*Array*/arr){
		if(!i3xx.isDefined(name))
			return null;
		
		var pstr = "";
		for(var i=0;i<arr.length;i++ ){
			pstr += ",arr["+i+"]";
		}
		if(pstr!="")
			pstr = pstr.substr(1);
			
		return eval("new "+name+"("+pstr+");");
	});
	
	/**
	 * dient zum Klonen eines Objekts 
	 * @param {Object} obj
	 */
	i3xx.define("i3xx.clone", function( obj ){
			for (var i in obj) {
				this[i] = obj[i];
			}
			//default HashCode l�schen (Indiz "hashCode" und "_hashCode" sind vorhanden)
			if(obj["hashCode"]!=null && obj["_hashCode"]!=null)
				this["hashCode"] = null;
			//neuen HashCode vergeben
			i3xx.hashCode(this);
		});
	
	/**
	 * Der HashCode wird mit FF00 initialisiert, folglich ist der erste Hash FF01.
	 */
	i3xx.define("i3xx._hashCode", 0xFF00);
	
	/**
	 * 
	 */
	i3xx.define("i3xx.hashCode", function( obj ){
			if(obj.hashCode==null){
				//create next default hashcode
				i3xx._hashCode++;
				//overflow
				if(i3xx._hashCode>=0xFFFFFFFF)
					i3xx._hashCode = 0xFF00;
				//add default hashcode
				obj._hashCode = i3xx._hashCode+"";
				//add function hashCode
				obj.hashCode = function() {
					return this._hashCode;
				};
			}
			return obj.hashCode();
		});
		
}