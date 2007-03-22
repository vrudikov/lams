# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-04-07 10:42:43
# 
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
'larsrc11',
'resourceService',
'Shared Resources',
'Shared Resources',
'sharedresources',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/larsrc11/learning/start.do?mode=learner',
'tool/larsrc11/learning/start.do?mode=author',
'tool/larsrc11/learning/start.do?mode=teacher',
'tool/larsrc11/authoring/start.do',
'tool/larsrc11/monitoring/summary.do',
'tool/larsrc11/definelater.do',
'tool/larsrc11/exportPortfolio?mode=learner',
'tool/larsrc11/exportPortfolio?mode=teacher',
'tool/larsrc11/contribute.do',
'tool/larsrc11/moderate.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11',
'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',
'lams-tool-larsrc11.jar',
'/org/lamsfoundation/lams/tool/rsrc/rsrcApplicationContext.xml',
NOW(),
NOW()
)
