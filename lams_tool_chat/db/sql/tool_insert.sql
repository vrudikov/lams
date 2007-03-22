-- CVS ID: $Id$
 
INSERT INTO lams_tool
(
tool_signature,
service_name,
tool_display_name,
description,
tool_identifier,
tool_version,
learning_library_id,
default_tool_content_id,
valid_flag,
grouping_support_type_id,
supports_run_offline_flag,
learner_url,
learner_preview_url,
learner_progress_url,
author_url,
monitor_url,
define_later_url,
export_pfolio_learner_url,
export_pfolio_class_url,
contribute_url,
moderation_url,
help_url,
language_file,
classpath_addition,
context_file,
create_date_time,
modified_date_time
)
VALUES
(
'lachat11',
'chatService',
'Chat',
'Chat',
'chat',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/lachat11/learning.do?mode=learner',
'tool/lachat11/learning.do?mode=author',
'tool/lachat11/learning.do?mode=teacher',
'tool/lachat11/authoring.do',
'tool/lachat11/monitoring.do',
'tool/lachat11/authoring.do?mode=teacher',
'tool/lachat11/exportPortfolio?mode=learner',
'tool/lachat11/exportPortfolio?mode=teacher',
'tool/lachat11/contribute.do',
'tool/lachat11/moderate.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/lachat11',
'org.lamsfoundation.lams.tool.chat.ApplicationResources',
'lams-tool-lachat11.jar',
'/org/lamsfoundation/lams/tool/chat/chatApplicationContext.xml',
NOW(),
NOW()
)
