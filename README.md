SDFEconomy
==========

SDFEconomy is a multiverse/location aware Vault based economy plugin used on the [SDF Minecraft Server](http://sdf.org/?minecraft). The plugin can configured to have separate economies corresponding to the separate inventory locations from any of these plugins:

* [Multiverse-Inventories](http://dev.bukkit.org/server-mods/multiverse-inventories/)
* [MultiInv](http://dev.bukkit.org/server-mods/multiinv/) 
* [WorldInventories](http://dev.bukkit.org/server-mods/world-inventories/)

In addition to multiverse support there are options for having an economy per world or a single economy for all worlds.

As Vault is not itself multiverse aware, we sometimes need to add specific support for other Vault plugins so money is placed in the correct location. So far we support:  

* [ChestShop](http://dev.bukkit.org/server-mods/chestshop/)

You will need at least [Vault version 1.2.3](http://dev.bukkit.org/server-mods/vault/files/37-vault-1-2-23/) or higher to use this plugin.

Builds can be found on the [Bukkit Page](http://dev.bukkit.org/server-mods/sdfeconomy/).

License
-------

SDFEconomy is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

SDFEconomy is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along
with SDFEconomy. If not, see http://www.gnu.org/licenses/.

Compilation
-----------

This plugin has a Maven 3 pom.xml and uses Maven to compile. Dependencies are 
therefore managed by Maven. You should be able to build it with Maven by running

    mvn package

a jar will be generated in the target folder. For those unfamilliar with Maven
it is a build system, see http://maven.apache.org/ for more information.

Known caveats
-------------

ChestShop through Vault checks the last location of an offline player or
current location of an online player to see if they have enough money to
buy items instead of directly using the location of the shop.
