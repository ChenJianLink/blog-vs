window._bd_share_main.F.module("view/select_view", function (e, t, n) {
    var r = e("base/tangram").T, i = e("base/class").Class, s = e("conf/const"), o = e("view/view_base"), u, a, f, l,
        c = function (e) {
            var t = "";
            return document.selection ? t = document.selection.createRange().text : t = document.getSelection(), r.string(t.toString()).trim()
        }, h = "getSelection" in document ? function () {
            document.getSelection().removeAllRanges(), l = ""
        } : function () {
            document.selection.empty(), l = ""
        };
    t.View = i.create(function (e) {
        function o() {
            if (e.bdCustomStyle) {
                var t = document.createElement("link");
                t.href = e.bdCustomStyle, t.rel = "styleSheet", t.type = "text/css", document.getElementsByTagName("head")[0].appendChild(t)
            } else window._bd_share_main.F.use("share_style" + n + "_" + i + ".css")
        }

        function p(e, n) {
            var i = n.bdMini || 2, s = n.bdSelectMiniList || t._partnerSort.slice(0, 4), o = [];
            r.each(s, function (e, t) {
                o[e] = '<a href="#" class="bds_' + t + '" data-cmd="' + t + '"></a>'
            }), f.find(".bdselect_share_partners").html(o.join(""))
        }

        function d(e, t) {
            var n = e.pageY, i = e.pageX;
            n += 5, i -= 18;
            var s = u.outerHeight(), o = r(window).scrollTop();
            if (n + s > r("body").height() && n + s > r(window).height() || n + s > o + r(window).height()) n = e.pageY - s - 5, n = n < o ? o : n;
            var a = t.bdPopupOffsetLeft, f = t.bdPopupOffsetTop;
            if (a || f) n += f | 0, i += a | 0;
            return {top: n, left: i}
        }

        function g(e, n) {
            var i = d(e, n);
            if (l.length < 5) {
                t.hide("less");
                return
            }
            r.each([u, a], function (e, t) {
                t.css({top: i.top, left: i.left}).show(), n.bdText = c()
            });
            var s = f.find("a").length, o = r(f.find("a")).outerWidth(!0), h = o * s + 20,
                p = parseInt(u.css("max-width"));
            p && h > p && (h = p), u.width(h), u.find(".bdselect_share_head").width(h), a.width(h), a.height(u.height());
            var g = u.find(".bdselect_share_dialog_search");
            g.attr("href", "http://www.baidu.com/s?wd=" + n.bdText + "&tn=SE_hldp08010_vurs2xrp");
            var y = m(function () {
                v("http://s.share.baidu.com/select?" + r.ajax.param({
                    log_type: "click",
                    content: encodeURIComponent(n.bdText)
                }))
            }, 100);
            g.click(y), h < 220 ? u.find(".bdselect_share_dialog_search_span").hide() : u.find(".bdselect_share_dialog_search_span").show(), v("http://s.share.baidu.com/select?" + r.ajax.param({
                log_type: "show",
                content: encodeURIComponent(n.bdText)
            }))
        }

        var t = this;
        t._container = null;
        var n = e.bdStyle || 0, i = "|16|24|32|".indexOf("|" + e.bdSize + "|") > -1 ? e.bdSize : 16, s = !1;
        t._buttonType = 0, t.render = function () {
            var s = "bdSharePopup_selectshare" + +(new Date),
                o = ['<iframe frameborder="0" id="' + s + 'bg" class="bdselect_share_bg" style="display:none;"></iframe>'].join(""),
                l = ['<div id="' + s + 'box" style="display:none;" share-type="selectshare" class="bdselect_share_box">', '<div class="selectshare-mod-triangle"><div class="triangle-border"></div><div class="triangle-inset"></div></div>', '<div  class="bdselect_share_head" ><span>\u5206\u4eab\u5230</span>', '<a href="http://www.baidu.com/s?wd=' + e.bdText + '&tn=SE_hldp08010_vurs2xrp"', ' class="bdselect_share_dialog_search" target="_blank">', '<i class="bdselect_share_dialog_search_i"></i>', '<span class="bdselect_share_dialog_search_span">\u767e\u5ea6\u4e00\u4e0b</span></a>', '<a class="bdselect_share_dialog_close"></a></div>', '<div class="bdselect_share_content" >', '<ul class="bdselect_share_list">', '<div class="bdselect_share_partners"></div>', '<a href="#" class="bds_more"  data-cmd="more"></a>', "</ul>", "</div>", "</div>"].join("");
            r("body").insertHTML("beforeEnd", o + l), t._container = u = r("#" + s + "box"), f = u.find(".bdselect_share_list").addClass("bdshare-button-style" + n + "-" + i), a = r("#" + s + "bg"), t._entities.push(u), r(".bdselect_share_dialog_close").click(t.hide)
        }, t.hide = function (e) {
            e || h(), a && a.hide(), u && u.hide()
        }, t._init = function () {
            var n;
            e.bdContainerClass ? n = r("." + e.bdContainerClass) : n = r("body").children(), r("body").on("mouseup", function (i) {
                n.each(function (n, s) {
                    s == i.target || r(s).contains(i.target) || !e.bdContainerClass && i.target == document.body ? setTimeout(function () {
                        l = c(), o(), t.show(i, e)
                    }, 10) : u.css("display") == "block" && t.hide()
                })
            })
        }, t.show = function (e, n) {
            window._bd_share_main.F.use(["component/partners", "share_popup.css", "select_share.css"], function (r) {
                t._partnerSort = r.partnerSort, s || (p(t._container, n), s = !0), g(e, n)
            })
        };
        var v = function () {
            var e = {};
            return function (t) {
                var n = "bdsharelog__" + (new Date).getTime(), r = e[n] = new Image;
                r.onload = r.onerror = function () {
                    e[n] = null
                }, r.src = t + "&t=" + (new Date).getTime(), r = null
            }
        }(), m = function (e, t, n) {
            var r, i, s, o = null, u = 0;
            n || (n = {});
            var a = function () {
                u = n.leading === !1 ? 0 : new Date, o = null, s = e.apply(r, i), o || (r = i = null)
            };
            return function () {
                var f = new Date;
                !u && n.leading === !1 && (u = f);
                var l = t - (f - u);
                return r = this, i = arguments, l <= 0 || l > t ? (clearTimeout(o), o = null, u = f, s = e.apply(r, i), o || (r = i = null)) : !o && n.trailing !== !1 && (o = setTimeout(a, l)), s
            }
        };
        t._distory = function () {
            u.remove(), a.remove()
        }
    }, o.ViewBase)
});