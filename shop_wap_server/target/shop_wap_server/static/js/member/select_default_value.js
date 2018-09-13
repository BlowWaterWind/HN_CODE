/**
 * 初始化带有默认值的选择列表。
 */
$(function() {
	$.fn.extend({
		initDefaultValueForSelect : function() {
			var options = this.children("option");
			if(options.length > 0) {
				var defaultValue = this.attr("data-default_value");
				this.removeAttr("data-default_value");
				this.removeData("default_value");
				if(!defaultValue!=null) {
					options.each(function(i) {
						var op = $(this);
						if(defaultValue == op.val()) {
							op.attr("selected", "selected");
							return false;
						} else {
							return true;
						}
					});
				}
				this.change();
			}
		}
	});
	$("select[data-default_value]").each(function(i) {
		$(this).initDefaultValueForSelect();
	});
});