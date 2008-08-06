# packages/lams2conf/www/tooladapter_forum.tcl

ad_page_contract {
    
    Tool Adapter for Forum
    
    @author Ernie Ghiglione (ErnieG@mm.st)
    @creation-date 2008-08-05
    @arch-tag: 2432DA0D-5EBF-4C94-BE8D-D339FD6F5487
    @cvs-id $Id$
} {
    method
    extToolContentID
    ts
    un
    hs
    cs
} -properties {
} -validate {
} -errors {
}

# Prepare all variables to compare hashs for authentication

set timestamp $ts
set username $un
set hash $hs
set course_id $cs

set datetime $timestamp

set lams_server_url [parameter::get -parameter lams_server_url -package_id [ad_conn package_id]]
set server_key [parameter::get -parameter server_key -package_id [ad_conn package_id]]
set server_id [parameter::get -parameter server_id -package_id [ad_conn package_id]]

set plaintext [string tolower [concat $timestamp$username$server_id$server_key]]
set hashValue [string tolower [ns_sha1 $plaintext]]


# Compare authentication hash
if {![string equal [string tolower $hash] $hashValue]} {

    # if authentication fails...
    ns_log Warning "LAMSint: userinfo request hash authentication failed. localhash: $hashValue requesthash: $hash"
    ns_write "HTTP/1.1 401 Usernameauthenticated"
    ad_script_abort
}

# Hash comparison OK, so continue...

switch $method {

    clone {
	
	# clones the instance in the course_id and the username
	set new_extToolContentID [forum::lams::clone_instance -forum_id $extToolContentID -course_id $course_id -user_id $username]

	# send the new extToolContentID to LAMS

	ReturnHeaders "text/plain"
	ns_write $new_extToolContentID
	ad_script_abort

    }

    import {
	# gets the file from LAMS with the content of the forum
    }

    export {
	# exports the content of the forum 

	# gets the content of the forum
	forum::get -forum_id $forum_id -array forum

	# exports the array to text
	set export_forum_as_string [array get forum]

	# returns the text from the array
	ns_returnfile 200 text/plain $export_forum_as_string
	ad_script_abort

    }

    export_portfolio {

	# ...

    }


    export_portfolio_class {

	# ...
    }

    get_outputs {

	# ...

    }

}


