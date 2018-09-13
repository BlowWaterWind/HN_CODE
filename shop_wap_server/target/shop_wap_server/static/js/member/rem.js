// JavaScript Document
(function (doc, win) {
          var docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
              var b = docEl.clientWidth;
              if (!b) return;
			      b= b>=640?640 : b;
			 var arem=b/16;
              docEl.style.fontSize =arem+ 'px';
            };

          if (!doc.addEventListener) return;
          win.addEventListener(resizeEvt, recalc, false);
          doc.addEventListener('DOMContentLoaded', recalc, false);
        })(document, window);