/*
 */
package com.github.omwah.SDFEconomy;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Implementation of PlayerAccount that utilizes the YamlFileConfiguration backend
 */
public class YamlPlayerAccount extends YamlAccount implements PlayerAccount { 

    public YamlPlayerAccount(ConfigurationSection section) {
        super(section);
    }

}
