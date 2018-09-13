function toggleModal(modal) {
    if ($("#" + modal)) {
        $("#" + modal).toggle();
    }
}

$(function() {
	 var swiper = new Swiper('.index-swiper', {
	 	initialSlide :1,
        effect: 'coverflow',
        slidesPerView: 3,
        grabCursor: true,
        centeredSlides: true,
        slidesPerView: 'auto',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
      coverflow: {
            rotate: 0,
            stretch: 25,
            depth: 60,
            modifier: 4,
            slideShadows : false
        }
    });
});