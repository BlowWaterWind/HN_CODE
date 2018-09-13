/**
 * the common js of the project
 *
 * @author: xxxx
 * @version: 2016/10/27 10:51
 *           $Id$
 */
$(function () {
if ($('#top-scroll li').length>3){$('#top-scroll').vTicker({
    speed: 500,
    pause: 2000,
    animation: 'fade',
    mousePause: false,
    showItems:3
});}
    if ($('#my-scroll li').length>8) {
        $('#my-scroll').vTicker({
            speed: 500,
            pause: 2000,
            animation: 'fade',
            mousePause: false,
            showItems: 8
        });
    }
    $('.popup-close,.popup-close-btn').click(function() {
        $('.bargin-mask,.bargin-popup').hide();

    })
});