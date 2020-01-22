/*
Name: 			View - Contact
Written by: 	Okler Themes - (http://www.okler.net)
Version: 		3.1.1
*/

(function() {

	"use strict";

	var Contact = {

		initialized: false,

		initialize: function() {

			if (this.initialized) return;
			this.initialized = true;

			this.build();
			this.events();

		},

		build: function() {

			// Containers
			var map = $("#googlemaps"),
				contactForm = $("#contactForm");

			// Validations Form Type
			if(contactForm.get(0)) {

				if(contactForm.data("type") == "advanced") {
					this.advancedValidations();
				} else {
					this.basicValidations();
				}

			}

		},

		events: function() {



		},

		advancedValidations: function() {

			var submitButton = $("#contactFormSubmit"),
				contactForm = $("#contactForm");

			submitButton.on("click", function() {
				if(contactForm.valid()) {
					submitButton.button("loading");
				}
			});

			contactForm.validate({
				onkeyup: false,
				onclick: false,
				onfocusout: false,
				rules: {
					name: {
						required: true
					},
					email: {
						required: true,
						email: true
					},
					subject: {
						required: true
					},
					message: {
						required: true
					},
					captcha: {
						required: true,
						captcha: true
					},
					'checkboxes[]': {
						required: true
					}
				},
				highlight: function (element) {
					$(element)
						.parent()
						.removeClass("has-success")
						.addClass("has-error");
				},
				success: function (element) {
					$(element)
						.parent()
						.removeClass("has-error")
						.addClass("has-success")
						.find("label.error")
						.remove();
				}
			});

			$.validator.addMethod("captcha", function () {
				var captchaValid = false;
				var phpquery = $.ajax({
					url: "php/contact-form-verify-captcha.php",
					type: "POST",
					async: false,
					dataType: "json",
					data: {
						captcha: $.trim($("#contactForm #captcha").val())
					},
					success: function (data) {
						if (data.response == "success") {
							captchaValid = true;
						} else {

						}
					}
				});
				if (captchaValid) {
					return true;
				}
			}, "");

		},

		basicValidations: function() {

			var contactform = $("#contactForm"),
				url = contactform.attr("action");

			contactform.validate({
				submitHandler: function(form) {

					// Loading State
					var submitButton = $(this.submitButton);
					submitButton.button("loading");

					// Ajax Submit
					$.ajax({
						type: "POST",
						url: url,
						data: {
							"name": $("#contactForm #name").val(),
							"email": $("#contactForm #email").val(),
							"subject": $("#contactForm #subject").val(),
							"message": $("#contactForm #message").val()
						},
						dataType: "json",
						success: function (data) {
							if (data.response == "success") {

								$("#contactSuccess").removeClass("hidden");
								$("#contactError").addClass("hidden");

								// Reset Form
								$("#contactForm .form-control")
									.val("")
									.blur()
									.parent()
									.removeClass("has-success")
									.removeClass("has-error")
									.find("label.error")
									.remove();

								if(($("#contactSuccess").position().top - 80) < $(window).scrollTop()){
									$("html, body").animate({
										 scrollTop: $("#contactSuccess").offset().top - 80
									}, 300);
								}

							} else {

								$("#contactError").removeClass("hidden");
								$("#contactSuccess").addClass("hidden");

								if(($("#contactError").position().top - 80) < $(window).scrollTop()){
									$("html, body").animate({
										 scrollTop: $("#contactError").offset().top - 80
									}, 300);
								}

							}
						},
						complete: function () {
							submitButton.button("reset");
						}
					});
				},
				rules: {
					name: {
						required: true
					},
					email: {
						required: true,
						email: true
					},
					subject: {
						required: true
					},
					message: {
						required: true
					}
				},
				highlight: function (element) {
					$(element)
						.parent()
						.removeClass("has-success")
						.addClass("has-error");
				},
				success: function (element) {
					$(element)
						.parent()
						.removeClass("has-error")
						.addClass("has-success")
						.find("label.error")
						.remove();
				}
			});

		}

	};

	Contact.initialize();

})();