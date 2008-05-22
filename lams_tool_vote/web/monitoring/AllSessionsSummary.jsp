<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>



				<table class="forms">

						<c:if test="${fn:length(voteGeneralMonitoringDTO.summaryToolSessions) > 2 }">
							<%-- When grouping is not enabled, we have only 2 items in summaryToolSessions.  The main toolSession and 'All' --%>
							
							<c:if test="${statsTabActive != 'true'}"> 							
								<tr> 
									<td colspan=2 align=center>
	
							 			<c:if test="${(voteGeneralMonitoringDTO.requestLearningReport != 'true')}"> 	
											<table>			
												<tr> 
													<td align=center valign=top> <b> <fmt:message key="label.group"/> </b>
															<select name="monitoredToolSessionId" onchange="javascript:submitSession(this.value,'submitSession');">
															<c:forEach var="toolSessionName" items="${voteGeneralMonitoringDTO.summaryToolSessions}">
																<c:forEach var="toolSessionId" items="${voteGeneralMonitoringDTO.summaryToolSessionsId}">
																	<c:out value="${toolSessionName.key}"/> -<c:out value="${toolSessionId.value}"/>
																
																		<c:if test="${toolSessionName.key == toolSessionId.key}"> 			
																	
																			<c:set var="SELECTED_SESSION" scope="request" value=""/>
																			<c:if test="${requestScope.selectionCase == 2}"> 			
																				<c:set var="currentMonitoredToolSession" scope="request" value="All"/>
																			</c:if>						
																			
																			<c:if test="${toolSessionId.value == requestScope.currentMonitoredToolSession}"> 			
																					<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
																			</c:if>						
																			
																			<c:if test="${toolSessionId.value != 'All'}"> 		
																				<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>> <c:out value="${toolSessionName.value}"/>  </option>						
																			</c:if>						
																			
																			<c:if test="${toolSessionId.value == 'All'}"> 	
																				<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>  All  </option>						
																			</c:if>						
																	</c:if>							
																</c:forEach>		  	
															</c:forEach>		  	
															</select>
														</td> 
													<tr>
												</table>
										</c:if>		
									</td> 
								</tr>
							</c:if>
						 
						
							<c:if test="${statsTabActive == 'true'}"> 							
								<tr> 
									<td colspan=2 align=center>
							 			<c:if test="${(voteGeneralMonitoringDTO.requestLearningReport != 'true')}"> 	
											<table>			
												<tr> 
													<td align=center valign=top> <b> <fmt:message key="label.group"/> </b>
															<select name="monitoredToolSessionIdStats" onchange="javascript:submitSession(this.value,'submitSession');">
															<c:forEach var="toolSessionName" items="${voteGeneralMonitoringDTO.summaryToolSessions}">
																<c:forEach var="toolSessionId" items="${voteGeneralMonitoringDTO.summaryToolSessionsId}">
																	<c:out value="${toolSessionName.key}"/> -<c:out value="${toolSessionId.value}"/>
																
																		<c:if test="${toolSessionName.key == toolSessionId.key}"> 			
																	
																			<c:set var="SELECTED_SESSION" scope="request" value=""/>
																			<c:if test="${requestScope.selectionCase == 2}"> 			
																				<c:set var="currentMonitoredToolSession" scope="request" value="All"/>
																			</c:if>						
																			
																			<c:if test="${toolSessionId.value == requestScope.currentMonitoredToolSession}"> 			
																					<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
																			</c:if>						
																			
																			<c:if test="${toolSessionId.value != 'All'}"> 		
																				<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>> <c:out value="${toolSessionName.value}"/>  </option>						
																			</c:if>						
																			
																			<c:if test="${toolSessionId.value == 'All'}"> 	
																				<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>  All  </option>						
																			</c:if>						
																	</c:if>							
																</c:forEach>		  	
															</c:forEach>		  	
															</select>
														</td> 
													<tr>
												</table>
										</c:if>		
									</td> 
								</tr>
							</c:if>
						</c:if> 
						
						<c:forEach var="currentDto" items="${voteGeneralMonitoringDTO.listVoteAllSessionsDTO}">
						
								<tr>
							 		<td  colspan=2 > <b>  <fmt:message key="label.groupName"/> </b>
							 		<c:out value="${currentDto.sessionName}"/>  </td>
								</tr>
						
								<tr> <td  colspan=2> 									
						  	 		<c:set var="currentSessionId" scope="request" value="${currentDto.sessionId}"/>
						  	 		
 							 			<table class="align-left">
						  					<tr>
										 		<td > <b>  <fmt:message key="label.total.students"/> </b> </td>
										 		<td>  <c:out value="${currentDto.sessionUserCount}"/> </td>
											</tr>
					
						  					<tr>
										 		<td > <b>  <fmt:message key="label.total.completed.students"/> </b> </td> 
										 		<td >  <c:out value="${currentDto.completedSessionUserCount}"/> 
											 		&nbsp(<c:out value="${currentDto.completedSessionUserPercent}"/> <fmt:message key="label.percent"/>)
										 		</td>
											</tr>
					
											<tr>
										 		<td  colspan=2> </td>
											</tr>
						  				
											<tr>
										 		<td  colspan=2> 
							                            <c:out value="${voteGeneralMonitoringDTO.activityInstructions}" escapeXml="false"/> 
												</td>
											</tr>
											
					
											<tr>
										 		<td  colspan=2 > </td>
											</tr>
											


											<tr>
												<td  colspan=2 valign=top class="align-left">
												<table align=center>
													<c:if test="${statsTabActive != 'true'}"> 							
														<tr> 
															<td> </td>
															<td  valign=top class="align-left" >
																<c:set scope="request" var="viewURL">
																	<html:rewrite page="/chartGenerator?type=pie&currentSessionId=${currentSessionId}"/>
																</c:set>
																<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
																	 <fmt:message key="label.view.piechart"/>  
																</a>
															</td>
														</tr>
														<tr> 
															<td> </td>
															<td  valign=top class="align-left" >
																<c:set scope="request" var="viewURL">
																	<html:rewrite page="/chartGenerator?type=bar&currentSessionId=${currentSessionId}"/>
																</c:set>
																<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
																	 <fmt:message key="label.view.barchart"/>  
																</a>
															</td>
														</tr>
													</c:if> 

												<tr>
											 		<td > <b>  <fmt:message key="label.nomination"/> </b> </td>
													<td > <b>  <fmt:message key="label.total.votes"/> </b> </td>
												</tr>
												
												<c:forEach var="currentNomination" items="${currentDto.mapStandardNominationsHTMLedContent}">
										  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
										  	 		 <tr>
							  	 						<td  valign=top class="align-left">
															<c:out value="${currentNomination.value}" escapeXml="false"/>
														 </td>
						


														<td  valign=top class="align-left">				  	 		
												  	 		<c:forEach var="currentUserCount" items="${currentDto.mapStandardUserCount}">
													  	 		<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
												  				<c:if test="${currentNominationKey == currentUserKey}"> 				
												  				
												  					<c:if test="${currentUserCount.value != '0' }"> 	
															  	 		<c:forEach var="currentQuestionUid" items="${currentDto.mapStandardQuestionUid}">
																  	 		<c:set var="currentQuestionUidKey" scope="request" value="${currentQuestionUid.key}"/>
															  				<c:if test="${currentQuestionUidKey == currentUserKey}"> 				
			
			
																	  	 		<c:forEach var="currentSessionUid" items="${currentDto.mapStandardToolSessionUid}">
																		  	 		<c:set var="currentSessionUidKey" scope="request" value="${currentSessionUid.key}"/>
																	  				<c:if test="${currentSessionUidKey == currentQuestionUidKey}"> 				
					
																		  				<c:if test="${currentNomination.value != 'Open Vote'}"> 				
																							<c:set scope="request" var="viewURL">
																								<lams:WebAppURL/>monitoring.do?dispatch=getVoteNomination&questionUid=${currentQuestionUid.value}&sessionUid=${currentSessionUid.value}
																							</c:set>
																		  																					
																							<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
																								 <c:out value="${currentUserCount.value}"/>  
																							</a>
																						</c:if> 	    
																		  				<c:if test="${currentNomination.value == 'Open Vote'}"> 				
																								 <c:out value="${currentUserCount.value}"/>  
																						</c:if> 	    
																						
																					</c:if> 	    
																				</c:forEach>		  
			
			
																			</c:if> 	    
																		</c:forEach>		  
																	</c:if> 	    								
													  				<c:if test="${currentUserCount.value == 0 }"> 		  				
																			 <c:out value="${currentUserCount.value}"/>  
																	</c:if> 	
																	    																								
																</c:if> 	    
															</c:forEach>		  
						
												  	 		<c:forEach var="currentRate" items="${currentDto.mapStandardRatesContent}">
													  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
												  				<c:if test="${currentNominationKey == currentRateKey}"> 				
																			 &nbsp(<c:out value="${currentRate.value}"/> <fmt:message key="label.percent"/>) 
																</c:if> 	    
															</c:forEach>		  
														</td>			
												</tr>	
												</c:forEach>	
										</table>
										</td>
									</tr>


							<tr>
						 		<td  colspan=2 > </td>
							</tr>
						
	
							<c:if test="${currentDto.existsOpenVote == 'true' }"> 			
							
								<tr>
									
							 		<td  colspan=2> <hr size="1"> </td>
								</tr>
	
								<tr>
							 		<td  colspan=2 > </td>
								</tr>
							
								<tr>
							 		<td  colspan=2>
						 			<table class="align-left">
												<tr>
											 		<th  colspan=3>  <fmt:message key="label.openVotes"/>  </th>
												</tr>
	
												<tr> 
													 <td  valign=top class="align-left"> <b>  <fmt:message key="label.vote"/>  </b> </td>  														 
													 <td  valign=top class="align-left"> <b>   <fmt:message key="label.user"/>  </b> </td>  
							  						 <td  valign=top class="align-left"> <b>   <fmt:message key="label.attemptTime"/> </b></td>
							 						 <c:if test="${statsTabActive != 'true'}"> 															  						 
								  						 <td  valign=top class="align-left"> <b>   <fmt:message key="label.visible"/>  </b></td>								  						 
													 </c:if> 																					  						 
									  			</tr>				 
											
						 			
											<c:forEach var="dtoEntry" items="${currentDto.listUserEntries}">
						  							<c:forEach var="questionAttemptData" items="${dtoEntry.questionAttempts}">
											  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
		  	 									  	 		<c:set var="currentUid" scope="request" value="${userData.uid}"/>
														<tr> 
																<td  valign=top class="align-left"> 
																	<c:out value="${dtoEntry.question}" escapeXml="false"/> 
																	<c:if test="${userData.visible != 'true' }"> 			
												                                 <i><fmt:message key="label.hidden"/> </i> 
																	</c:if> 								
																</td>
																 
																 <td  valign=top class="align-left">    <c:out value="${userData.userName}"/>   </td>  
																 <td  valign=top class="align-left">    <lams:Date value="${userData.attemptTime}"/>  </td>
																 
										 						<c:if test="${statsTabActive != 'true'}"> 							
																	 <td  valign=top class="align-left">
												 						<c:if test="${userData.visible == 'true' }"> 			
													                                <html:submit property="hideOpenVote" 
													                                             styleClass="linkbutton" 
											                                                     onclick="submitOpenVote(${currentUid}, 'hideOpenVote');">						                                             
													                                    <fmt:message key="label.hide"/>
													                                </html:submit>
																		</c:if> 													
							
																		<c:if test="${userData.visible != 'true' }"> 			
													                                <html:submit property="showOpenVote" 
													                                             styleClass="linkbutton" 
											                                                     onclick="submitOpenVote(${currentUid}, 'showOpenVote');">						                                             
													                                    <fmt:message key="label.show"/>
													                                </html:submit>
																		</c:if> 						
																	</td>																			
															</c:if> 	    																	
																 
														</tr>		
													</c:forEach>		  	
											</c:forEach>		
									</table> 
									</td>
								</tr>
							 </c:if> 	
							 
		 					<tr> <td  colspan=2>  </td>
							</tr>
		 					<tr> <td  colspan=2> <hr size="2"></td>
							</tr>
		 					<tr> <td  colspan=2>  </td>
							</tr>
							 
						</table>
						</td> </tr>	

					</c:forEach>		
							
					<tr>
				 		<td  colspan=2 >  </td>
					</tr>

					</table>					

