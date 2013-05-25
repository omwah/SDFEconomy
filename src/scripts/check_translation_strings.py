#!/usr/bin/env python

# Goes through code looking for translation calls and ensures that the keys to the ResourceBundle
# appears in the translation file

import os
import re
import sys
from glob import glob

# Base path is one level up from where script is run from, the src dir
base_dir = os.path.join(os.path.dirname(sys.argv[0]), "..")

java_files = glob(os.path.join(base_dir, "main/java/com/github/omwah/SDFEconomy/commands/*.java"))
translation_files = glob(os.path.join(base_dir, "main/resources/*.properties"))
show_unused = False

print "No output means everything is fine."

# Go through java files and find ResourceBundle keys
resource_keys = {}
for src_file in java_files:
    with open(src_file) as file_obj:
        contents = file_obj.read()

    class_match = re.search("class\s+(\w+)", contents)
    if class_match:
        class_name = class_match.groups()[0]
    else:
        print "Could not find class name in file:", src_file
        continue

    # Matches calls to getTranslation
    trans_matches = re.findall("getTranslation\s*\(\s*\"((?:[^\"\\\\]|\\\\.)*)\"", contents, re.MULTILINE)
    
    # Matches calls to getClassTranslation
    # Prefixes with class name
    trans_class_matches = re.findall("getClassTranslation\s*\(\s*\"((?:[^\"\\\\]|\\\\.)*)\"", contents, re.MULTILINE)
    trans_matches += [ "%s-%s" % (class_name, m) for m in trans_class_matches ]

    # For each unique key name add a list of files containing that key name
    for match in trans_matches:
        m_val = resource_keys.get(match, [])
        m_val.append(src_file)
        resource_keys[match] = m_val 

# Check that keys are found in property files
# Property files may contain keys that are not used
for prop_file in translation_files:
    prop_keys = []
    with open(prop_file) as file_obj:
        for file_line in file_obj:
            file_line = file_line.strip()

            if len(file_line) == 0 or file_line.find("#") == 0:
                # Skip comments and 
                continue

            key, value = file_line.split("=", 1)
            prop_keys.append(key.strip())
        
        # Check that resource_keys are in property file
        for r_key, r_filenames in resource_keys.items():
            if not r_key in prop_keys:
                print '\n'.join(r_filenames)
                print "\t%s not found in translation file: %s" % (r_key, prop_file)
                print
        # Check for unused properties keys, this is not necessarily a problem
        if show_unused:
            for p_key in prop_keys:
                if not p_key in resource_keys.keys():
                    print "Resource key %s in translation file: %s is unused" % (p_key, prop_file)
