﻿
(function( $ ) {
		$.widget( "custom.combobox", {
			_create: function() {
				this.wrapper = $( "<span>" )
					.addClass( "combox_input" )
					.insertAfter( this.element );
				
				this.element.hide();
				this._createAutocomplete();
				this._createShowAllButton();
				
				
				var width = jQuery(this.element).outerWidth() - 18;
				this.wrapper.outerWidth(jQuery(this.element).outerWidth());
				this.input.outerWidth(width);
			},
			
			disable: function(bool){
				if(bool){
					jQuery(this.element).attr('disabled', 'disabled');
					jQuery(this.input).attr('disabled', 'disabled');
					jQuery(this.button).attr('disabled', 'disabled');
				}
			},
			
			setValue: function(value){
				this.element.val(value);
				var seleted = this.element.children( ":selected" ).text();
			    this.input.val(seleted);
			},
			
			setText: function(text){
				var value2 = this.element.find('option').filter(function() {
				    return jQuery(this).text() == text; 
				}).val();
				
				this.input.val(text);
				this.element.val(value2);
			},
			
			rebindData: function(){
				this.input.autocomplete({
					delay: 0,
					minLength: 0,
					source: $.proxy( this, "_source" )
				});
			},
			
			_createAutocomplete: function() {
				var selected = this.element.children( ":selected" ),
					value = selected.val() ? selected.text() : "";
				var selectedElement = this.element;
				
				this.input = $( "<input>" )
					.appendTo( this.wrapper )
					.val( value )
					.attr( "id", "combox_transform_" + this.element.attr("name") )
					.removeClass( "ui-autocomplete-input" )
					.addClass( "combox_text")
					.autocomplete({
						delay: 0,
						minLength: 0,
						source: $.proxy( this, "_source" )
					});
				
				this.input.focus(function(){selectedElement.trigger("focus")})
				
				//this.input.val( this.element.find("option:selected").text());

				this._on( this.input, {
					autocompleteselect: function( event, ui ) {
						ui.item.option.selected = true;
						this._trigger( "select", event, {
							item: ui.item.option
						});
						this.element.val(ui.item.option.value);
						this.element.trigger("onchange"); 
						this.element.trigger("change");
						this.element.trigger("blur"); 
					},

					autocompletechange: "_removeIfInvalid"
				});
			},

			_createShowAllButton: function() {
				var input = this.input,
					wasOpen = false;

				var selectedElement = this.element;
				
				this.button = $( "<a>" )
					.attr( "tabIndex", -1 )
					.tooltip()
					.appendTo( this.wrapper )
					.removeClass( "ui-corner-all ui-sta-default ui-button ui-widget ui-button-icon-only" )
					.addClass( "combox_corner" )
					.mousedown(function() {
						wasOpen = input.autocomplete( "widget" ).is( ":visible" );
					})
					.click(function() {
						input.focus();

						// Close if already visible
						if ( wasOpen ) {
							return;
						}

						// Pass empty string as value to search for, displaying all results
						input.autocomplete( "search", "" );
						
						selectedElement.trigger("focus");
					});
			},

			_source: function( request, response ) {
				var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
				response( this.element.children( "option" ).map(function() {
					var text = $( this ).text();
					if ( this.value && ( !request.term || matcher.test(text) ) )
						return {
							label: text,
							value: text,
							option: this
						};
				}) );
			},

			_removeIfInvalid: function( event, ui ) {

				// Selected an item, nothing to do
				if ( ui.item ) {
					return;
				}

				// Search for a match (case-insensitive)
				var value = this.input.val().trim(),
					valueLowerCase = value.toLowerCase(),
					valid = false;
				this.element.children( "option" ).each(function() {
					if ( $( this ).text().toLowerCase() === valueLowerCase ) {
						this.selected = valid = true;
						return false;
					}
				});

				// Found a match, nothing to do
				if ( valid ) {
					return;
				}
				alert(value + " 没有对应的匹配值");
				// Remove invalid value
				
				this.input.val( "" );//.tooltip( "open" );
				
				this.element.val( "" );
				
				/*this._delay(function() {
					this.input.tooltip( "close" ).attr( "title", "" );
				}, 2500 );
				this.input.autocomplete( "instance" ).term = "";*/
			},
			
			_destroy: function() {
				this.wrapper.remove();
				this.element.show();
			}

		});
})( jQuery );

function setCombox(){
	//20150311 Hinson begin
	var curFormID = jQuery("input[name='curformId']").val();
	var formSystemId = jQuery("input[name='formSystemId'][type='hidden']").val();

	if(formSystemId=='30283'){
		jQuery("select[name!='field_9_19']"+
				"[name!='field_9_4'][name!='field_9_6']"+
				"[name!='field_9_8'][name!='MemCHGProduct_id']"+
				"[name!='MemCHGPlan_id'][name!='field_9_13']").attr("class","combox");
		
		jQuery( ".combox" ).combobox();
	}
	else if(curFormID!='AU_StampDuty_Cal' && curFormID!='Pr_Request_FM_1'
		&& curFormID!='Prop_confirm'){	//AU SD form not need to use the new combox
	//20150311 Hinson end		
		jQuery("select").attr("class","combox");
		
		jQuery( ".combox" ).combobox();
	//20150311 Hinson begin
	}
	//20150311 Hinson end
	
}

setCombox();

