 <%@page import="com.my.app.AdminController"%>
<%@ page contentType="text/html;charset=utf-8"%> 
		<script>
		
			jQuery(document).ready(function() {
				Main.init();
				SVExamples.init();
				//FormValidator.init();
				//FormElements.init();
				//UIModals.init();
				
			});
            function vform()
            {
            	$("#form").validate({ 
		            errorElement: "span", // contain the error msg in a span tag
		            errorClass: 'help-block',
		            errorPlacement: function (error, element) { // render error placement for each input type
		                 
		                    error.insertAfter(element);
		                     
		            },
			       rules: {
					   username: "required",
					   email: { 
					        email: true
					   },
		               password: {
		                    minlength: 5,
		                    required: true
		                },
		                password_again: {
		                    required: true,
		                    minlength: 5,
		                    equalTo: "#password"
		                },
		                theindex: {
		                    required: true,
		                    number: true
		                }
				        },
			      messages: {
					   username: "请输入姓名",
					   email:"请输入正确的email地址.."
					    
			      },
			        invalidHandler: function (event, validator) { //display error alert on form submit
		            	
		                //successHandler1.hide();
		                //errorHandler1.show();
		            },
		            highlight: function (element) {
		                $(element).closest('.help-block').removeClass('valid');
		                // display OK icon
		                $(element).closest('.form-group').removeClass('has-success').addClass('has-error').find('.symbol').removeClass('ok').addClass('required');
		                // add the Bootstrap error class to the control group
		            },
		            unhighlight: function (element) { // revert the change done by hightlight
		                $(element).closest('.form-group').removeClass('has-error');
		                // set error class to the control group
		            },
		            success: function (label, element) {
		            	//alert(1);
		                label.addClass('help-block valid');
		                // mark the current input as valid and display OK icon
		                $(element).closest('.form-group').removeClass('has-error').addClass('has-success').find('.symbol').removeClass('required').addClass('ok');
		              
		            }			        
				});            	
            	
            }
			/*
			function loadPage(url,m1,m2)
			{ 
				loadUrl(url);
				goActive(m1,m2);
				//$("#tname").html(tname);
				//$("#smname").html(smname);
				//$("#nname").html(tname);
				
			 }*/
			function loadPage(url,furl,m1,m2)
			{ 
 
				
				
				loadUrl(url);
				$('#form').attr("action", furl)
				goActive(m1,m2);
				
				//$("#tname").html(tname);
				//$("#smname").html(smname);
				//$("#nname").html(tname);
				
			 }
			function loadUrl(url)
			{ 
			var ajaxCallUrl=url;
			$.ajax({
				cache: true,
				type: "POST",
				url:ajaxCallUrl,
				data:$('#form').serialize(),// 你的formid
				async: true,
				error: function(request) {
				alert(ajaxCallUrl);
					alert("Connection error");
				},
				success: function(data) {
					//alert(data);
					if(data.indexOf("DOCTYPE html")==-1){
					$("#form").html(data); 
					Main.init();
					//FormElements.init();
					/*$("input[name='group.theindex']").TouchSpin({
						verticalbuttons: true
					});*/
					//FormValidator.init();
					 vform();
					 FormElements.init();
					}else
					 {
						 if(data.indexOf("非法访问")>-1)
						 {
							 location.href="/error/auth"; 
						 }else {
							 location.href="/login/"; 
						 }
						 
					 }
				}
				});
			 }
			function doUrl(url,msg)
			{ 
			var ajaxCallUrl=url;
			$.ajax({
				cache: true,
				type: "POST",
				url:ajaxCallUrl,
				data:$('#form').serialize(),// 你的formid
				async: true,
				error: function(request) {
				alert(ajaxCallUrl);
					alert("Connection error");
				},
				success: function(data) {
					//alert(data);
					if(data.indexOf("DOCTYPE html")==-1){
					$("#form").html(data); 
					swal("Good!", msg, "success");
					}else
					 {
						 location.href="/login/";
					 }
				}
				});
			 }
			
			
			var main_menu=new Array(<%for(int i=0;i<AdminController.marray1.length;i++){out.print((i!=0?"":"")+"\""+AdminController.marray1[i]+"\",");};out.print("\"m_sec\"");%>);
			var children_menu=new Array(<%for(int i=0;i<AdminController.marray2.length;i++){out.print((i!=0?"":"")+"\""+AdminController.marray2[i]+"\",");}out.print("\"m_sec_user\"");%>);
			function goActive(m_m,c_m)
			{
				 for (var i=0;i<main_menu.length;i++)
				{
					//alert(main_menu[i]+1);
					$("#"+main_menu[i]).removeClass("open")
					//$("#"+main_menu[i]).removeClass("active"); 
					//alert(main_menu[i]+2);
				} 
				for (var i=0;i<children_menu.length;i++)
				{
					$("#"+children_menu[i]).removeClass("active");
				}
				 $("#"+m_m).addClass("open").addClass("active");
				 $("#"+c_m).addClass("active");  
				
			}
			 function selectAll(o)
			 {
			  
			   if(o.checked)
			   {
			     var checks=document.getElementsByName("ids");
			     for (i=0;i<checks.length;i++)
				 { 
				   checks[i].checked =true; 
				 } 

			   }else
			   {
			     var checks=document.getElementsByName("ids");
			     for (i=0;i<checks.length;i++)
				 { 
				   checks[i].checked =false; 
				 }    
			   }
			 }
			 function delinfos(url,m1,m2){
				    var checks=document.getElementsByName("ids");
				    var c=0;
				    for (i=0;i<checks.length;i++)
					 { 
					   if(checks[i].checked) 
					   {
					    c++;
					   } 
					 } 
				  if(c==0)
				  {
				    alert("请选中一条记录选行删除！");
				  }	 
				  else
				  {
				  delinfo(url,-1,m1,m2);
				  }
			 }
			 function delinfo(url,id,m1,m2)
			 {
			 if(confirm("您确认要删除选中的记录吗?"))
			 {
			 	var ajaxCallUrl=url+"?id="+id;
			 	doUrl(ajaxCallUrl,"您已成功删除选中的记录");
			 	 }
			 }
			 loadPage('${url}','#','${m1}','${m2}'); 
			
			 //loadPage('/sec/user/input','m_sec','m_sec_user','用户信息','查看及管理用户信息'); 
			 //var jsonstr={"firstname":"aa"};
			// var json=JSON.parse(jsonstr);
			//alert($("#firstname").val());
			//$("#firstname").val(jsonstr.firstname);
		 
			
		</script>
		