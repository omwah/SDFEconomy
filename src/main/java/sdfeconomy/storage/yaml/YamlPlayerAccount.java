/*
 */

package sdfeconomy.storage.yaml;

import org.bukkit.configuration.ConfigurationSection;
import sdfeconomy.storage.PlayerAccount;

/**
 * Implementation of PlayerAccount that utilizes the YamlFileConfiguration backend
 */
public class YamlPlayerAccount extends YamlAccount implements PlayerAccount
{

    public YamlPlayerAccount(ConfigurationSection section) {
        super(section);
    }

}
