$(function() {
	var CHILD_CATEGORY_BY_PARENT_ID = {};
	var backgroundCategoryDataLoadUrl = changeUrl;
	var backgroundCategoryDataField = {nameFieldName:"orgName", valueFieldName:"orgId"};
	var top = $("#" + _GOODS_CATEGORY_SELECT_PARAMETER_NAME + "-0");
	
	if(!isNull(_CATEGORY_SELECT_INITOR)) {
		_CATEGORY_SELECT_INITOR(top);
	}
	
	bindCategoryChangeListener(top, backgroundCategoryDataLoadUrl, backgroundCategoryDataField);
	top.change();

	function bindCategoryChangeListener(select, childDataLoadUrl, childDataField) {
		select.change(function() {
			var level = parseInt(select.data("category_level"), 10);
			resetChildSelect(level + 1, select.val(), childDataLoadUrl, childDataField);
		});
	}
	
	function resetChildSelect(level, parentValue, childDataLoadUrl, childDataField) {
		removeAllChildSelect(level);
		loadChildValues(level, parentValue, childDataLoadUrl, childDataField);
	}
	
	function removeAllChildSelect(level) {
		$("select[name='" + _GOODS_CATEGORY_SELECT_PARAMETER_NAME + "'][data-category_level!='" + 0 + "']").each(function(i) {
			var select = $(this);
			var cateLevel = parseInt(select.data("category_level"), 10);
			if(!isNaN(cateLevel)) {
				if(cateLevel >= level) {
					select.remove();
				}
			}
		});
	}
	
	function loadChildValues(level, parentValue, childDataLoadUrl, childDataField) {
		loadSelectValues("address", parentValue, childDataLoadUrl, function(values) {
			createChildWithValues(level, values, childDataLoadUrl, childDataField);
		});
	}
	
	function loadSelectValues(key, parentValue, dataLoadUrl, callback) {
		if(isEmpty(parentValue)) {
			callback();
			return;
		}
		if(!CHILD_CATEGORY_BY_PARENT_ID[key]) {
			CHILD_CATEGORY_BY_PARENT_ID[key] = {};
		}
		var childValues = CHILD_CATEGORY_BY_PARENT_ID[key][parentValue];
		if(!isNull(childValues)) {
			callback(childValues);
		} else {
			ajaxRequest( dataLoadUrl, function(res) {
				CHILD_CATEGORY_BY_PARENT_ID[key][parentValue] = res.data;
				callback(res.data);
			}, null, {parentValue:parentValue});
		}
	}
	
	function createChildWithValues(level, values, childDataLoadUrl, childDataField) {
		if(!isEmpty(_BRAND_SELECT_ELEMENT_ID)) {
			setSelectValue($("#" + _BRAND_SELECT_ELEMENT_ID), null, null);
		}
		if(!isNull(values) && values.length > 0) {
			var child = createCategorySelect(level, childDataLoadUrl, childDataField);
			setSelectValue(child, values, childDataField);
		}
		if(!isEmpty(_BRAND_SELECT_ELEMENT_ID)) {
			level = parseInt(level, 10);
			if(level > 0) {
				var parent = $("#" + _GOODS_CATEGORY_SELECT_PARAMETER_NAME + "-" + (level - 1));
				var brandDataLoadUrl = changeUrl;
				var brandDataField = {nameFieldName:"orgName", valueFieldName:"orgId"};
				loadSelectValues("address", parent.val(), brandDataLoadUrl, function(values) {
					setSelectValue($("#" + _BRAND_SELECT_ELEMENT_ID), values, brandDataField);
				});
			}
		}
	}
	
	function createCategorySelect(level, childDataLoadUrl, childDataField) {
		var selectName = _GOODS_CATEGORY_SELECT_PARAMETER_NAME;
		var id = selectName + '-' + level;
		var select = '<select id="' + id + '" name="' + selectName + '" data-category_level="' + level + '"  class="input-medium"';
		var defaultValue = $("#defaultValue-" + id).val();
		if(!isEmpty(defaultValue)) {
			select += ' data-default_value="' + defaultValue + '"';
		}
		select += '></select>';
		var parentSelect = $("#" + selectName + "-" + (level - 1));
		parentSelect.after(select);
		
		var cateSelect = $("#" + id);
		bindCategoryChangeListener(cateSelect, childDataLoadUrl, childDataField);
		
		if(!isNull(_CATEGORY_SELECT_INITOR)) {
			_CATEGORY_SELECT_INITOR(cateSelect);
		}
		
		return cateSelect;
	}
	
	function setSelectValue(select, values, childDataField) {
		select.empty();
		select.append("<option value=''>请选择</option>");
		if(!isEmpty(values)) {
			for(var i = 0; i < values.length; ++i) {
				var value = values[i];
				var option = "<option value='" + value[childDataField.valueFieldName] + "'>" + value[childDataField.nameFieldName] + "</option>";
				select.append(option);
			}
			select.initDefaultValueForSelect();
		}
	}
});