!function (t) {
    var e = {};

    function i(n) {
        if (e[n]) return e[n].exports;
        var r = e[n] = {i: n, l: !1, exports: {}};
        return t[n].call(r.exports, r, r.exports, i), r.l = !0, r.exports
    }

    i.m = t, i.c = e, i.d = function (t, e, n) {
        i.o(t, e) || Object.defineProperty(t, e, {enumerable: !0, get: n})
    }, i.r = function (t) {
        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(t, "__esModule", {value: !0})
    }, i.t = function (t, e) {
        if (1 & e && (t = i(t)), 8 & e) return t;
        if (4 & e && "object" == typeof t && t && t.__esModule) return t;
        var n = Object.create(null);
        if (i.r(n), Object.defineProperty(n, "default", {
            enumerable: !0,
            value: t
        }), 2 & e && "string" != typeof t) for (var r in t) i.d(n, r, function (e) {
            return t[e]
        }.bind(null, r));
        return n
    }, i.n = function (t) {
        var e = t && t.__esModule ? function () {
            return t.default
        } : function () {
            return t
        };
        return i.d(e, "a", e), e
    }, i.o = function (t, e) {
        return Object.prototype.hasOwnProperty.call(t, e)
    }, i.p = "", i(i.s = 0)
}([function (module, exports) {
    var mobile = "", si_n = "";
    if (window.webtrendsAsyncInit = function () {
        var dcs = (new Webtrends.dcs).init({
            domain: window.location.protocol.indexOf("https:") == 0 ? "www.hn.10086.cn:7099" : "www.hn.10086.cn:7098",
            dcsid: "v1/dcs5w0txb10000wocrvqy1nqm_6n1p",
            fpcdom: ".10086.cn",
            fpc: "WT_FPCN",
            timezone: 8,
            i18n: !0,
            dcsdelay: 1e3,
            plugins: {}
        });
        dcs.WT.branch = "wap_shop", si_n = document.title;
        var metas = window.parent.document.getElementsByTagName("meta");
        for (i = 0; i < metas.length; i++) "WT.mobile" == metas[i].getAttribute("name") && (mobile = metas[i].getAttribute("content"));
        mobile ? document.cookie = "mobile=" + encode_mobile(mobile) + ";path=/;domain=" + window.location.host.split(":")[0] : (mobile = document.cookie.replace(/(?:(?:^|.*;\s*)mobile\s*\=\s*([^;]*).*$)|^.*$/, "$1"), mobile && (is_mobile(mobile) ? mobile = mobile : is_mobile(decode_mobile(mobile)) && (mobile = decode_mobile(mobile)))), "" != mobile && void 0 != mobile && is_mobile(mobile) && (dcs.WT.mobile = mobile), dcs.track(), $(function () {
            $("body").on("click", "*", function (event) {
                var si_x = "", e = this, codeFlag = !0, currentPage = window.location.href, t = getUrlName(currentPage),
                    j;
                if ("a" == e.tagName || "input" == e.tagName || "button" == e.tagName || $(e).attr("onclick") || $._data && $._data(e, "events") && $._data(e, "events").click || "dropdown" == $(e).attr("data-toggle") || "model" == $(e).attr("data-toggle") || "tooltip" == $(e).attr("data-toggle") || "tab" == $(e).attr("data-toggle") || "collapse" == $(e).attr("data-toggle") || "popover" == $(e).attr("data-toggle") || "button" == $(e).attr("data-toggle")) {
                    if (e.type) t = e.value ? e.id + e.name ? "_" + e.name : "" + e.className ? "_" + e.className : "" + e.value ? "_" + e.value : "" : e.id + e.name ? "_" + e.name : "" + e.className ? "_" + e.className : "" + $(e).html() ? "_" + $(e).html() : "", j = e.type; else {
                        var apath = "";
                        e.href && (apath = splitPath(e.href));
                        var manCenter = "", imgSrc = "";
                        manCenter = (Trim_udbac_shaggy($(e).attr("class")) ? Trim_udbac_shaggy($(e).attr("class")) : "") + ($(e).find("p").length > 0 ? "_" + Trim_udbac_shaggy($(e).find("p").text()) : ""), $(e).find("img").length > 0 && (imgSrc = getSrc($(e).find("img").eq(0).attr("src"))), 0 == $(e).children().length ? t += (e.id ? "_" + e.id : "") + (apath ? "_" + apath : "") + manCenter + "_" + $(e).html() + (imgSrc ? "_" + imgSrc : "") : t += (e.id ? "_" + e.id : "") + (apath ? "_" + apath : "") + manCenter + (imgSrc ? "_" + imgSrc : ""), j = "Link"
                    }
                    if (si_x = t, $(e).attr("onclick")) if ($(e).attr("onclick").indexOf("dcsMultiTrack") > -1) codeFlag = !1; else {
                        var thisNameArr = $(e).attr("onclick").split(";");
                        codeFlag = hasPoint(thisNameArr)
                    }
                    if ($(e).attr("href") && $(e).attr("href").split("javascript:")[1] && codeFlag) {
                        var allFunctions = $(e).attr("href").split("javascript:")[1],
                            allFunctionsArr = allFunctions.split(";");
                        codeFlag = hasPoint(allFunctionsArr)
                    }
                    if ("function" == typeof $._data && codeFlag && $._data(this, "events") && $._data(this, "events").click) {
                        // console.log($._data(this, "events").click), console.log($._data(this, "events").click[0].handler);
                        for (var i = 0; i < $._data(this, "events").click.length; i++) {
                            var thisCurrentString = "" + eval($._data(this, "events").click[i].handler);
                            thisCurrentString.indexOf("dcsMultiTrack") > -1 && (codeFlag = !1)
                        }
                    }
                    codeFlag && ("" != si_x && "" != si_n ? _tag.dcsMultiTrack("DCS.dcsuri", "/nopv.gif", "WT.event", Trim_udbac_shaggy(t), "WT.si_n", si_n, "WT.si_x", si_x, "WT.obj", j) : _tag.dcsMultiTrack("DCS.dcsuri", "/nopv.gif", "WT.event", Trim_udbac_shaggy(t), "WT.obj", j))
                }
            })
        })
    }, !window._tag) {
        function _wt() {
        }

        _wt.prototype.trackEvent = function () {
        }, _wt.prototype.E = function (t, e) {
            for (var i = t.target || t.srcElement; i.tagName && i.tagName.toLowerCase() != e.toLowerCase();) i = (i = i.parentElement || i.parentNode) || {};
            return i
        }, _wt.prototype.P = function (t) {
            var e = t.clientX, i = t.clientY;
            return $j = void 0 != document.documentElement && 0 != document.documentElement.clientHeight ? document.documentElement : document.body, e + (void 0 == window.pageXOffset ? $j.scrollLeft : window.pageXOffset) + "x" + (i + (void 0 == window.pageYOffset ? $j.scrollTop : window.pageYOffset))
        }, _wt.prototype.N = function (t) {
            var e, i, n, r = "", a = "", s = ["div", "table"], o = s.length;
            for (e = 0; e < o && !((n = s[e]).length && (r = (i = this.E(t, n)).getAttribute && i.getAttribute("id") ? i.getAttribute("id") : "", a = i.className || "", r.length || a.length)); e++) ;
            return r.length ? r : a
        }, Function.prototype.wtbind = function (t) {
            var e = this;
            return function () {
                return e.apply(t, arguments)
            }
        }, _wt.prototype.dcsMultiTrack = function () {
            window.Webtrends && Webtrends.multiTrack({argsa: arguments, delayTime: 100})
        };
        var _tag = new _wt
    }

    function Trim_udbac_shaggy(t) {
        return "" == t || void 0 == t ? "" : t.replace(/\s+/g, "")
    }

    function getParam(t, e) {
        if (e) {
            if (-1 == (n = e).indexOf("?")) return "";
            var i = n.split("?")[1].replace(/^\?/, "").split("&")
        } else {
            var n;
            i = (n = location.search.replace(/^\?/, "")).split("&")
        }
        var r = {};
        if (i.length > 0) for (var a = 0, s = i.length; a < s; a++) {
            r[(l = i[a].split("="))[0]] = l[1]
        }
        if (t) {
            if (r[t]) return r[t] || "";
            if (-1 != n.indexOf("redirect_uri")) {
                for (var o = n.split("&"), c = 0; c < o.length; c++) if (-1 != o[c].indexOf("redirect_uri")) i = unescape(o[c].replace("redirect_uri=", "")).split("&");
                for (a = 0, s = i.length; a < s; a++) {
                    var l;
                    r[(l = i[a].split("="))[0]] = l[1]
                }
                return r[t] || ""
            }
        }
        return ""
    }

    function splitPath(t) {
        var e = "", i = "";
        if (-1 != (e = t || window.location.href).indexOf("redirect_uri")) for (var n = e.split("?")[1].split("&"), r = 0; r < n.length; r++) {
            if (-1 != n[r].indexOf("redirect_uri")) i = unescape(n[r].replace("redirect_uri=", "")).split("?")[0]
        } else i = e.split("?")[0];
        if (i) {
            var a = i.split("/");
            if (a) return (a.length > 0 ? a[a.length - 1] : "") + "_" + (a.length > 1 ? a[a.length - 2] : "") || ""
        }
    }

    function is_mobile(t) {
        return /^(\+[0-9]{2,}-?)?1(3[0-9]|5[0-35-9]|8[0-9]|4[57]|7[678])[0-9]{8}$/.test(t)
    }

    function get_a_random() {
        var t = new Array("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        return String(t[parseInt(16 * Math.random(), 10)])
    }

    function pre_fix_integer(t, e) {
        return (Array(e).join(0) + t).slice(-e)
    }

    function encode_mobile(t) {
        var e = "abcdef";
        t = (t = String(t)).substring(0, 2) + get_a_random() + get_a_random() + t.substring(2, 5) + get_a_random() + get_a_random() + t.substring(5, 8) + get_a_random() + t.substring(8, 11);
        var i = String(parseInt("0x" + String(t.substring(0, 4))) ^ e),
            n = String(parseInt("0x" + String(t.substring(4, 8))) ^ e);
        return String(parseInt("0x" + String(t.substring(8, 12))) ^ e) + "-" + String(parseInt("0x" + String(t.substring(12, 16))) ^ e) + "-" + i + "-" + n
    }

    function decode_mobile(t) {
        var e = "abcdef";
        t = t.split("-");
        var i = pre_fix_integer(Number(t[0] ^ e).toString(16), 4),
            n = pre_fix_integer(Number(t[1] ^ e).toString(16), 4),
            r = pre_fix_integer(Number(t[2] ^ e).toString(16), 4) + pre_fix_integer(Number(t[3] ^ e).toString(16), 4) + i + n;
        return r.substring(0, 2) + r.substring(4, 7) + r.substring(9, 12) + r.substring(13, 17)
    }

    function getSrc(t) {
        if (t) {
            var e = t.split("/");
            if (e.length > 0) return e[e.length - 2] + "_" + e[e.length - 1].split(".")[0]
        }
    }

    function hasPoint(thisArr) {
        for (var thisFlag = !0, i = 0; i < thisArr.length; i++) if (thisArr[i].split("(")[0]) {
            var thisCurrentFunction = thisArr[i].split("(")[0];
            if ("function" == typeof thisCurrentFunction || "string" == typeof thisCurrentFunction) {
                var thisCurrentString = "" + eval(thisCurrentFunction);
                if (thisCurrentString.indexOf("dcsMultiTrack") > -1) {
                    thisFlag = !1;
                    break
                }
            } else thisFlag = !0
        } else thisFlag = !0;
        return thisFlag
    }

    function getUrlName(t) {
        var e = t.split(".html")[0].split("/");
        return t.split("/").length > 2 && (thisPageName = e[e.length - 2] + "_" + e[e.length - 1]), thisPageName
    }

    var l = void 0;
    !function (t, i, n, r) {
        function a(t) {
            return t && (t.forEach || (t.forEach = function (t, e) {
                for (var i = e || window, n = 0, r = this.length; n < r; ++n) t.call(i, this[n], n, this)
            }), t.filter || (t.filter = function (t, e) {
                for (var i = e || window, n = [], r = 0, a = this.length; r < a; ++r) t.call(i, this[r], r, this) && n.push(this[r]);
                return n
            }), t.indexOf || (t.indexOf = function (t) {
                for (var e = 0; e < this.length; ++e) if (this[e] === t) return e;
                return -1
            })), t
        }

        if (!t.Ra) {
            var o = {
                f: {}, xa: 0, e: {}, addEventListener: t.addEventListener ? function (t, e, i) {
                    t.addEventListener && t.addEventListener(e, i, !1)
                } : function (t, e, i) {
                    t.attachEvent && t.attachEvent("on" + e, i, !1)
                }, g: {}, version: "2.1.0", j: {}, Fa: !1, m: 25, H: function () {
                    r.search && (o.j = o.S(r.search)), t.webtrendsAsyncInit && !t.webtrendsAsyncInit.hasRun && (t.webtrendsAsyncInit(), t.webtrendsAsyncInit.hasRun = !0), o.addEventListener(t, "load", function () {
                        o.Fa = !0
                    })
                }, o: function (t) {
                    return "[object Function]" === Object.prototype.toString.call(t)
                }, Ia: function (t) {
                    var e, i = [];
                    for (e in t) t.hasOwnProperty(e) && "" != t[e] && t[e] != l && "function" != typeof t[e] && i.push({
                        k: e,
                        v: t[e]
                    });
                    return i
                }, extend: function (t, e, i) {
                    for (key in e) (i || void 0 === t[key]) && (t[key] = e[key]);
                    return t
                }, find: function (t) {
                    return o.Y || (o.Y = o.Da()), o.Y(t)
                }, Da: function () {
                    var e = (e = /MSIE (\d+)/.exec(n.userAgent)) ? e[1] : 99;
                    if (i.querySelectorAll && i.body && e > 8) {
                        var r = i.body;
                        return function (t) {
                            return r.querySelectorAll(t)
                        }
                    }
                    return t.jQuery ? t.jQuery.find : t.Sizzle ? t.Sizzle : t.YAHOO && YAHOO.aa && YAHOO.aa.ba ? YAHOO.aa.ba.bb : "qwery" in t ? qwery : (t.YUI && YUI().eb("node", function (t) {
                        return t.all
                    }), i.querySelectorAll && (r = i.body) ? function (t) {
                        return r.querySelectorAll(t)
                    } : function () {
                        return []
                    })
                }, S: function (t) {
                    t = t.split(/[&?]/g);
                    var e = {};
                    try {
                        for (var i = 0, n = t.length; i < n; ++i) {
                            var r = t[i].match(/^([^=]+)(?:=([\s\S]*))?/);
                            if (r && r[1]) {
                                var a = decodeURIComponent(r[1]);
                                e[a] ? (e[a] = [e[a]], e[a].push(decodeURIComponent(r[2]))) : e[a] = decodeURIComponent(r[2])
                            }
                        }
                    } catch (t) {
                        this.D.push(t), this.B(t)
                    }
                    return e
                }, Ga: function (t, e, n) {
                    arguments.length < 2 && (e = !0), s = i.createElement("script"), s.type = "text/javascript", s.async = e, s.src = t, s2 = i.getElementsByTagName("script")[0], s2.parentNode.insertBefore(s, s2)
                }, O: function (t, e) {
                    var i = t.target || t.srcElement;
                    for ("string" == typeof e && (e[e] = 1); i && i.tagName && !e[i.tagName.toUpperCase()];) i = i.parentElement || i.parentNode;
                    return i
                }, K: function (t) {
                    return "function" == typeof encodeURIComponent ? encodeURIComponent(t) : escape(t)
                }, Ha: function (t) {
                    for (var e in o.f) o.f[e].L(t);
                    return !1
                }, s: function (t, e, i) {
                    return e || (e = "collect"), i ? o.t("transform." + e, t, i) : o.t("transform." + e, t), this
                }, t: function (t, e, r) {
                    function s(e, i) {
                        o.g[t][e.i] || (o.g[t][e.i] = a([])), o.g[t][e.i].push(i)
                    }

                    if (t && e && "" != t && o.o(e)) {
                        if ("wtmouseup" === t && (t = "wtmouse"), "wtmouse" === t && !o.U) {
                            var c = /MSIE (\d+)/.exec(n.userAgent);
                            o.addEventListener(i, (c ? c[1] : 99) >= 8 ? "mousedown" : "mouseup", function (e) {
                                e || (e = window.event), o.ia(t, {event: e})
                            }), o.U = !0
                        }
                        if (o.g[t] || (o.g[t] = {}), r) s(r, e); else for (dcsid in o.f) s(o.f[dcsid], e)
                    }
                }, ia: function (t, e) {
                    for (dcsid in o.f) o.fireEvent(t, o.f[dcsid], e)
                }, ca: function (t, e, i, n) {
                    if ("function" == typeof e) return e.onetime ? (i.push(e), !0) : (e(t, n), !1)
                }, fireEvent: function (t, e, i) {
                    var n = a([]);
                    if (o.g[t] && o.g[t][e.i]) {
                        if (!(t = o.g[t][e.i]).length) return;
                        for (var r = t.length - 1; r >= 0; r--) o.ca(e, t[r], n, i) && t.pop()
                    }
                    n.forEach(function (t) {
                        t(e, i)
                    })
                }
            }, c = o.fireEvent, h = o.t;
            o.b = function () {
                return this.Q = t.RegExp ? /dcs(uri)|(ref)|(aut)|(met)|(sta)|(sip)|(pro)|(byt)|(dat)|(p3p)|(cfg)|(redirect)|(cip)/i : "", this.X = {}, this.d = this.WT = {}, this.h = this.DCS = {}, this.l = this.DCSext = {}, this.i = this.dcssID = "dcsobj_" + o.xa++, this.images = this.images = [], this.D = this.errors = [], this.a = this.FPCConfig = {}, this.c = this.TPCConfig = {}, this.Xa = {}, this.images = [], this.ab = [], this.Sa = [], this.Qa = !1, this.F = this.R = "", this.U = !1, this
            }, o.b.prototype = {
                H: function (e) {
                    function i(t, i) {
                        return e.hasOwnProperty(t) ? e[t] : i
                    }

                    function n(t, e, i) {
                        return t && t.hasOwnProperty(e) ? t[e] : i
                    }

                    return this.Ua = e, this.B = i("errorlogger", function () {
                    }), this.wa = this.dcsid = e.dcsid, this.q = this.queue = i("queue", []), this.domain = this.domain = i("domain", ".mysite.cn"), this.Oa = this.timezone = i("timezone", -8), this.enabled = this.enabled = i("enabled", !0), this.G = this.i18n = i("i18n", !0), this.X = t.RegExp ? this.G ? {
                        "%25": /\%/g,
                        "%26": /\&/g,
                        "%23": /\#/g
                    } : {
                        "%09": /\t/g,
                        "%20": / /g,
                        "%23": /\#/g,
                        "%26": /\&/g,
                        "%2B": /\+/g,
                        "%3F": /\?/g,
                        "%5C": /\\/g,
                        "%22": /\"/g,
                        "%7F": /\x7F/g,
                        "%A0": /\xA0/g
                    } : "", e.metanames && (this.T = a(e.metanames.toLowerCase().split(","))), this.r = this.vtid = i("vtid", l), this.V = i("paidsearchparams", "gclid"), this.Na = this.splitvalue = i("splitvalue", ""), o.m = e.dcsdelay || o.m, this.ya = this.delayAll = i("delayAll", !1), this.W = this.preserve = i("preserve", !0), this.a.enabled = this.FPCConfig.enabled = n(e.FPCConfig, "enabled", !0), this.a.domain = this.FPCConfig.domain = n(e.FPCConfig, "domain", i("fpcdom", "")), this.a.name = this.FPCConfig.name = n(e.FPCConfig, "name", i("fpc", "WT_FPC")), this.a.n = this.FPCConfig.expiry = n(e.FPCConfig, "expires", i("cookieexpires", 63113851500)), this.a.n = this.a.n < 63113851500 ? this.a.n : 63113851500, this.a.Aa = new Date(this.getTime() + this.a.n), this.a.Ma = 0 === this.a.n, this.c.enabled = this.TPCConfig.enabled = n(e.TPCConfig, "enabled", !i("disablecookie", !0)), this.c.u = this.TPCConfig.cfgType = n(e.TPCConfig, "cfgType", this.c.enabled ? "" : "1"), e.cookieTypes && ("none" === e.cookieTypes.toLowerCase() ? (this.a.enabled = !1, this.c.enabled = !1, this.c.u = "1") : "firstpartyonly" === e.cookieTypes.toLowerCase() ? (this.a.enabled = !0, this.c.enabled = !1, this.c.u = "1") : "all" === e.cookieTypes.toLowerCase() && (this.a.enabled = !0, this.c.enabled = !0, this.c.u = n(e.TPCConfig, "cfgType", ""))), this.Ya = this.fpc = this.a.name, this.Za = this.fpcdom = this.a.domain, this.Wa = this.cookieExp = this.a.n, i("privateFlag", !1) || (o.f[this.i] = this), o.e[this.domain] || (o.e[this.domain] = ""), !i("privateFlag", !1) && this.a.enabled ? this.oa(this.i) : this.I(), this
                }, pa: function (t) {
                    void 0 !== t && (!o.e[this.domain] && t.gTempWtId && (o.e[this.domain] = t.gTempWtId), this.F = t.gTempWtId, !o.e[this.domain] && t.gWtId && (o.e[this.domain] = t.gWtId), this.R = t.gWtAccountRollup), this.I()
                }, oa: function (t) {
                    return -1 == i.cookie.indexOf(this.a.name + "=") && -1 == i.cookie.indexOf("WTLOPTOUT=") && this.c.enabled ? (this.enabled && o.Ga("//" + this.domain + "/" + this.wa + "/wtid.js?callback=Webtrends.dcss." + t + ".dcsGetIdCallback", !0), !1) : (this.I(), !0)
                }, I: function () {
                    this.da || (c("onready", this), this.Ea(), this.La(), this.da = !0)
                }, Ea: function () {
                    for (var t = 0; t < this.q.length; t++) this.N(this.q[t]);
                    this.q = []
                }, La: function () {
                    var t = this;
                    this.q.push = function (e) {
                        t.N(e)
                    }
                }, s: function (t, e) {
                    o.s(t, e, this)
                }, ga: function (t, e) {
                    var i = this, n = o.extend({
                        domEvent: "click",
                        callback: l,
                        argsa: [],
                        args: {},
                        delayTime: l,
                        transform: l,
                        filter: l,
                        actionElems: {A: 1, INPUT: 1, BUTTON: 1},
                        finish: l
                    }, e, !0);
                    return h("wtmouse", function (e, r) {
                        i.ha(i, t, o.extend(r, n, !0))
                    }, i), this
                }, Z: function (t, e, i, n) {
                    e.element = i, "form" !== n && "input" !== n && "button" !== n || (e.domEvent = "submit"), t.J(e)
                }, ha: function (t, e, i) {
                    var n = o.find;
                    if (n && i.event && i.actionElems) {
                        var r = o.O(i.event, i.actionElems), a = r.tagName ? r.tagName.toLowerCase() : "";
                        if (e.toUpperCase() in i.actionElems && i.actionElems[a.toUpperCase()]) return this.Z(t, i, r, a);
                        if ((e = n(e)) && r && e && e.length) for (n = 0; n < e.length; n++) if (e[n] === r) {
                            this.Z(t, i, r, a);
                            break
                        }
                    }
                }, C: function (t, e) {
                    var n = a(i.cookie.split("; ")).filter(function (e) {
                        return -1 != e.indexOf(t + "=")
                    })[0];
                    return !(!n || n.length < t.length + 1) && (a(n.split(t + "=")[1].split(":")).forEach(function (t) {
                        t = t.split("="), e[t[0]] = t[1]
                    }), !0)
                }, ta: function (t, e, n) {
                    var r = [];
                    a(e = o.Ia(e)).forEach(function (t) {
                        r.push(t.k + "=" + t.v)
                    }), r = r.sort().join(":"), i.cookie = t + "=" + r + n
                }, qa: function (t, e, i, n) {
                    var r = {};
                    return this.C(t, r) ? e == r.id && i == r.lv && n == r.ss ? 0 : 3 : 2
                }, na: function () {
                    var t = {};
                    return this.C(this.a.name, t), t
                }, ma: function () {
                    if (-1 == i.cookie.indexOf("WTLOPTOUT=")) if (this.a.enabled) {
                        var t = this.d, e = this.a.name, n = new Date, r = 6e4 * n.getTimezoneOffset() + 36e5 * this.Oa;
                        n.setTime(n.getTime() + r);
                        var a = new Date(n.getTime());
                        t.co_f = t.vtid = t.vtvs = t.vt_f = t.vt_f_a = t.vt_f_s = t.vt_f_d = t.vt_f_tlh = t.vt_f_tlv = "";
                        var s = {};
                        if (this.C(e, s)) {
                            var c = s.id, l = parseInt(s.lv), h = parseInt(s.ss);
                            if (null == c || "null" == c || isNaN(l) || isNaN(h)) return;
                            t.co_f = c, c = new Date(l), t.vt_f_tlh = Math.floor((c.getTime() - r) / 1e3), a.setTime(h), (n.getTime() > c.getTime() + 18e5 || n.getTime() > a.getTime() + 288e5) && (t.vt_f_tlv = Math.floor((a.getTime() - r) / 1e3), a.setTime(n.getTime()), t.vt_f_s = "1"), n.getDate() == c.getDate() && n.getMonth() == c.getMonth() && n.getFullYear() == c.getFullYear() || (t.vt_f_d = "1")
                        } else {
                            if (this.F.length) t.co_f = o.e[this.domain].length ? o.e[this.domain] : this.F, t.vt_f = "1"; else if (o.e[this.domain].length) t.co_f = o.e[this.domain]; else {
                                for (t.co_f = "2", h = n.getTime().toString(), c = 2; c <= 32 - h.length; c++) t.co_f += Math.floor(16 * Math.random()).toString(16);
                                t.co_f += h, t.vt_f = "1"
                            }
                            0 == this.R.length && (t.vt_f_a = "1"), t.vt_f_s = t.vt_f_d = "1", t.vt_f_tlh = t.vt_f_tlv = "0"
                        }
                        t.co_f = escape(t.co_f), t.vtid = void 0 === this.r ? t.co_f : this.r || "", t.vtvs = (a.getTime() - r).toString(), r = (this.a.Ma ? "" : "; expires=" + this.a.Aa.toGMTString()) + "; path=/" + ("" != this.a.domain ? "; domain=" + this.a.domain : ""), n = n.getTime().toString(), a = a.getTime().toString(), s.id = t.co_f, s.lv = n, s.ss = a, this.ta(e, s, r), 0 != (e = this.qa(e, t.co_f, n, a)) && (t.co_f = t.vtvs = t.vt_f_s = t.vt_f_d = t.vt_f_tlh = t.vt_f_tlv = "", void 0 === this.r && (t.vtid = ""), t.vt_f = t.vt_f_a = e)
                    } else this.d.vt_f = "4", this.d.vtid = this.r ? this.r : ""
                }, Pa: function () {
                    try {
                        var t;
                        return arguments && arguments.length > 1 ? t = {argsa: Array.prototype.slice.call(arguments)} : 1 === arguments.length && (t = arguments[0]), void 0 === t && (t = {
                            element: l,
                            event: l,
                            Ta: []
                        }), void 0 === t.argsa && (t.argsa = []), this.P("collect", t), this
                    } catch (t) {
                        this.D.push(t), this.B(t)
                    }
                }, L: function (t) {
                    t && t.length > 1 && (t = {argsa: Array.prototype.slice.call(arguments)}), this.J(t)
                }, J: function (t) {
                    try {
                        return void 0 === t && (t = {}), this.P("multitrack", t), !1
                    } catch (t) {
                        this.D.push(t), this.B(t)
                    }
                }, ja: function () {
                    this.h = {}, this.d = {}, this.l = {}, arguments.length % 2 == 0 && this.z(arguments)
                }, z: function (t) {
                    if (t) for (var e = 0, i = t.length; e < i; e += 2) 0 == t[e].indexOf("WT.") ? this.d[t[e].substring(3)] = t[e + 1] : 0 == t[e].indexOf("DCS.") ? this.h[t[e].substring(4)] = t[e + 1] : 0 == t[e].indexOf("DCSext.") && (this.l[t[e].substring(7)] = t[e + 1])
                }, ua: function (t) {
                    var e, i;
                    if (this.W) {
                        this.p = [];
                        for (var n = 0, r = t.length; n < r; n += 2) 0 == (i = t[n]).indexOf("WT.") ? (e = i.substring(3), this.p.push(i, this.d[e] || "")) : 0 == i.indexOf("DCS.") ? (e = i.substring(4), this.p.push(i, this.h[e] || "")) : 0 == i.indexOf("DCSext.") && (e = i.substring(7), this.p.push(i, this.l[e] || ""))
                    }
                }, sa: function () {
                    this.W && (this.z(this.p), this.p = [])
                }, va: function () {
                    var e = new Date, a = this.d, s = this.h;
                    if (a.tz = parseInt(e.getTimezoneOffset() / 60 * -1) || "0", a.bh = e.getHours() || "0", a.ul = "Netscape" == n.appName ? n.language : n.fb, "object" == typeof screen && (a.cd = "Netscape" == n.appName ? screen.pixelDepth : screen.colorDepth, a.sr = screen.width + "x" + screen.height), "boolean" == typeof n.javaEnabled() && (a.jo = n.javaEnabled() ? "Yes" : "No"), i.title && (a.ti = t.RegExp ? i.title.replace(RegExp("^" + r.protocol + "//" + r.hostname + "\\s-\\s"), "") : i.title), a.js = "Yes", a.ct = "unknown", i.body && i.body.addBehavior) try {
                        i.body.addBehavior("#default#clientCaps"), a.ct = i.body.Va || "unknown", i.body.addBehavior("#default#homePage"), a.hp = i.body.$a(location.href) ? "1" : "0"
                    } catch (t) {
                        this.B(t)
                    }
                    var c = 0, l = 0;
                    if ("number" == typeof t.innerWidth ? (c = t.innerWidth, l = t.innerHeight) : i.documentElement && (i.documentElement.clientWidth || i.documentElement.clientHeight) ? (c = i.documentElement.clientWidth, l = i.documentElement.clientHeight) : i.body && (i.body.clientWidth || i.body.clientHeight) && (c = i.body.clientWidth, l = i.body.clientHeight), a.bs = c + "x" + l, this.G && (a.le = "string" == typeof i.defaultCharset ? i.defaultCharset : "string" == typeof i.characterSet ? i.characterSet : "unknown"), a.tv = o.version, a.sp = this.Na, a.dl = "0", o.j && o.j.Ba && (a.fb_ref = o.j.Ba), o.j && o.j.Ca && (a.fb_source = o.j.Ca), a.ssl = 0 == r.protocol.indexOf("https:") ? "1" : "0", s.dcsdat = e.getTime(), s.dcssip = r.hostname, s.dcsuri = r.pathname, a.es = s.dcssip + s.dcsuri, r.search && (s.dcsqry = r.search), s.dcsqry) for (e = s.dcsqry.toLowerCase(), c = this.V.length ? this.V.toLowerCase().split(",") : [], l = 0; l < c.length; l++) if (-1 != e.indexOf(c[l] + "=")) {
                        a.srch = "1";
                        break
                    }
                    "" == i.referrer || "-" == i.referrer || "Microsoft Internet Explorer" == n.appName && parseInt(n.appVersion) < 4 || (s.dcsref = i.referrer), s.dcscfg = this.c.u
                }, la: function (t, e) {
                    if ("" != e) {
                        if (null === t || t === l) return "";
                        var i;
                        t = t.toString();
                        for (i in e) e[i] instanceof RegExp && (t = t.replace(e[i], i));
                        return t
                    }
                    return escape(t)
                }, w: function (t, e) {
                    if (this.G && "" != this.Q && !this.Q.test(t)) if ("dcsqry" == t) {
                        for (var i = "", n = e.substring(1).split("&"), r = 0; r < n.length; r++) {
                            var a = (c = n[r]).indexOf("=");
                            if (-1 != a) {
                                var s = c.substring(0, a), c = c.substring(a + 1);
                                0 != r && (i += "&"), i += s + "=" + o.K(c)
                            }
                        }
                        e = e.substring(0, 1) + i
                    } else e = o.K(e);
                    return "&" + t + "=" + this.la(e, this.X)
                }, ka: function (n, r) {
                    if (i.images) {
                        var a = new Image;
                        this.images.push(a);
                        var s = !1;
                        o.o(r.callback) ? e = r.callback : e = function (t, e) {
                        }, g = this, a.onload = function () {
                            if (!s) return s = !0, e(g, r), !0
                        }, se = t.setTimeout(function () {
                            if (!s) return a.removeAttribute("src"), s = !0, e(g, r), !0
                        }, o.m), a.onload = function () {
                            if (!s) return clearTimeout(se), s = !0, e(g, r), !0
                        }, a.src = n
                    }
                }, ra: function () {
                    var t;
                    if (i.documentElement ? t = i.getElementsByTagName("meta") : i.all && (t = i.all.cb("meta")), void 0 !== t) for (var e = t.length, n = 0; n < e; n++) {
                        var r = t.item(n).name, a = t.item(n).content;
                        t.item(n), r.length > 0 && (0 == (r = r.toLowerCase()).toUpperCase().indexOf("WT.") ? this.d[r.substring(3)] = a : 0 == r.toUpperCase().indexOf("DCSEXT.") ? this.l[r.substring(7)] = a : 0 == r.toUpperCase().indexOf("DCS.") ? this.h[r.substring(4)] = a : this.T && -1 != this.T.indexOf(r) && (this.l["meta_" + r] = a))
                    }
                }, M: function (t) {
                    if (-1 == i.cookie.indexOf("WTLOPTOUT=")) {
                        var e = this.d, a = this.h, s = this.l, o = this.i18n,
                            c = "http" + (0 == r.protocol.indexOf("https:") ? "s" : "") + "://" + this.domain + ("" == this.dcsid ? "" : "/" + this.dcsid) + "/dcs.gif?";
                        for (var h in o && (e.dep = ""), a) "" != a[h] && a[h] != l && "function" != typeof a[h] && (c += this.w(h, a[h]));
                        for (h in e) "" != e[h] && e[h] != l && "function" != typeof e[h] && (c += this.w("WT." + h, e[h]));
                        for (h in s) "" != s[h] && s[h] != l && "function" != typeof s[h] && (o && (e.dep = 0 == e.dep.length ? h : e.dep + ";" + h), c += this.w(h, s[h]));
                        o && e.dep.length > 0 && (c += this.w("WT.dep", e.dep)), c.length > 2048 && n.userAgent.indexOf("MSIE") >= 0 && (c = c.substring(0, 2040) + "&WT.tu=1"), this.ka(c, t), this.d.ad = ""
                    }
                }, Ja: function () {
                    this.va(), this.ra(), this.Ka = !0
                }, getTime: function () {
                    return (new Date).getTime()
                }, za: 0, $: function (t) {
                    for (var e = this.getTime(); this.getTime() - e < t;) this.za++
                }, P: function (t, e) {
                    t || (t = "collect"), this.q.push({action: t, message: e})
                }, N: function (t) {
                    if (this.enabled) {
                        var e = "action_" + t.action, i = t.message;
                        if (this.Ka || this.Ja(), i.event && !i.element && (i.element = o.O(i.event, {A: 1})), !o.o(i.filter) || !i.filter(this, i)) {
                            if (i.args) for (var n in i.argsa = i.argsa || [], i.args) i.argsa.push(n, i.args[n]);
                            i.element && i.element.getAttribute && i.element.getAttribute("data-wtmt") && (i.argsa = i.argsa.concat(i.element.getAttribute("data-wtmt").split(","))), c("transform." + t.action, this, i), c("transform.all", this, i), i.transform && o.o(i.transform) && i.transform(this, i), this.ma(), o.o(this[e]) && this[e](i), c("finish." + t.action, this, i), c("finish.all", this, i), i.finish && o.o(i.finish) && i.finish(this, i)
                        }
                    }
                }, fa: function (t) {
                    var e = t && t.argsa && t.argsa.length % 2 == 0;
                    e && (this.ua(t.argsa), this.z(t.argsa)), this.h.dcsdat = this.getTime(), this.M(t), e && this.sa()
                }, ea: function (t) {
                    t && t.argsa && t.argsa.length % 2 == 0 && this.z(t.argsa), this.M(t)
                }
            }, o.b.prototype.action_multitrack = o.b.prototype.fa, o.b.prototype.action_collect = o.b.prototype.ea, t.Webtrends = o, t.WebTrends = o, t.WT = t.Webtrends, o.multiTrack = o.Ha, o.dcs = o.b, o.dcss = o.f, o.addTransform = o.s, o.bindEvent = o.t, o.getQryParams = o.S, o.dcsdelay = o.m, o.find = o.find, o.b.prototype.init = o.b.prototype.H, o.b.prototype.dcsMultiTrack = o.b.prototype.L, o.b.prototype.track = o.b.prototype.Pa, o.b.prototype.addSelector = o.b.prototype.ga, o.b.prototype.dcsGetIdCallback = o.b.prototype.pa, o.b.prototype.dcsCleanUp = o.b.prototype.ja, o.b.prototype.dcsGetFPC = o.b.prototype.na, o.b.prototype.addTransform = o.b.prototype.s, o.H()
        }
    }(window, window.document, window.navigator, window.location)
}]);