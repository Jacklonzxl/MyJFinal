 <%@ page contentType="text/html;charset=utf-8"%> 
<ul class="main-navigation-menu">
							<li>
								<a href="index.html"><i class="fa fa-home"></i> <span class="title"> Dashboard </span><span class="label label-default pull-right ">LABEL</span> </a>
							</li>
					 
							 
							 
						 
							<li  id="m_sec"  <%if(request.getParameter("m")==null||"m_sec".equals(request.getParameter("m"))) {%> class="active"<%} %>  >
								<a href="?m=m_sec"><i class="fa fa-user"></i> <span class="title">用户管理</span><i class="icon-arrow"></i> </a>
								<ul class="sub-menu">
								    <li id="m_sec_group" >
										<a href="javascript:loadPage('/sec/group','#','m_sec','m_sec_group')">
											<span class="title">用户分组 </span>
										</a>
									</li>
									<li id="m_sec_user" >
										<a href="javascript:loadPage('/sec/user','#','m_sec','m_sec_user')">
											<span class="title">用户信息 </span>
										</a>
									</li>
									<li id="m_sec_role">
										<a href="javascript:loadPage('/sec/role','#','m_sec','m_sec_role')">
											<span class="title">角色信息 </span>
										</a>
									</li>
									<li id="m_sec_permission">
										<a href="javascript:loadPage('/sec/permission','#','m_sec','m_sec_permission')">
											<span class="title">权限信息</span>
										</a>
									</li> 
								</ul>
							</li>
							
							<li id="m_biz" <%if("m_biz".equals(request.getParameter("m"))){%> class="active"<%} %> >
								<a href="?m=m_biz"><i class="fa fa-th-large"></i> <span class="title">商务管理 </span><i class="icon-arrow"></i> </a>
								<ul class="sub-menu"  ><!-- style="display: block;" -->
									<li id="m_biz_channel">
										<a href="javascript:loadPage('/biz/channel','#','m_biz','m_biz_channel')">
											<span class="title">渠道管理</span>
										</a>
									</li>
									<li id="m_biz_channelUrl">
										<a href="javascript:loadPage('/biz/channelUrl','#','m_biz','m_biz_channelUrl')">
											<span class="title">渠道链接</span>
										</a>
									</li>
									<li id="m_biz_channelSms">
										<a href="javascript:loadPage('/biz/channelSms','#','m_biz','m_biz_channelSms')">
											<span class="title">短信接口</span>
										</a>
									</li> 
									<li id="m_biz_cpTask">
										<a href="javascript:loadPage('/biz/cpTask','#','m_biz','m_biz_cpTask')">
											<span class="title">CP任务</span>
										</a>
									</li>  
									<li id="m_biz_cpTaskLiuc">
										<a href="javascript:loadPage('/biz/cpTaskLiuc','#','m_biz','m_biz_cpTaskLiuc')">
											<span class="title">留存任务</span>
										</a>
									</li>  
								</ul>
							</li>
							 	 
						</ul>