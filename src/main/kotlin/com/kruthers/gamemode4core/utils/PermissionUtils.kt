package com.kruthers.gamemode4core.utils

import com.kruthers.gamemode4core.Gamemode4Core
import net.luckperms.api.node.types.InheritanceNode
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun playerAddGroup(player: Player, group: String?): Boolean {
    if (Gamemode4Core.luckperms) {
        return lpPlayerAddGroup(player, group)
    }
    return vaultPlayerAddGroup(player, group)
}

fun playerRemoveGroup(player: Player, group: String?): Boolean {
    if (Gamemode4Core.luckperms) {
        return lpPlayerRemoveGroup(player, group)
    }
    return vaultPlayerRemoveGroup(player, group)
}

private fun vaultPlayerAddGroup(player: Player, group: String?): Boolean {
    if (Gamemode4Core.permission.groups.contains(group) && group != null) {
        Bukkit.getServer().worlds.forEach {
            if (it != null) {
                Gamemode4Core.permission.playerAddGroup(it.name, Bukkit.getOfflinePlayer(player.uniqueId),group)
            }
        }

    } else {
        return false
    }

    return true
}

private fun vaultPlayerRemoveGroup(player: Player, group: String?): Boolean {
    if (Gamemode4Core.permission.groups.contains(group) && group != null) {
        Bukkit.getServer().worlds.forEach {
            if (it != null) {
                Gamemode4Core.permission.playerRemoveGroup(it.name, Bukkit.getOfflinePlayer(player.uniqueId), group)
            }
        }

    } else {
        return false
    }

    return true
}

private fun lpPlayerAddGroup(player: Player, groupName: String?): Boolean {
    if (Gamemode4Core.permission.groups.contains(groupName) && groupName != null) {
        val user = Gamemode4Core.luckPermsAPI.userManager.loadUser(player.uniqueId).get()
        val group = Gamemode4Core.luckPermsAPI.groupManager.getGroup(groupName) ?: return false

        user.data().add(InheritanceNode.builder(group).build())
        Gamemode4Core.luckPermsAPI.userManager.saveUser(user)


    } else {
        return false
    }

    return true
}

private fun lpPlayerRemoveGroup(player: Player, groupName: String?): Boolean {
    if (Gamemode4Core.permission.groups.contains(groupName) && groupName != null) {
        val user = Gamemode4Core.luckPermsAPI.userManager.loadUser(player.uniqueId).get()
        val group = Gamemode4Core.luckPermsAPI.groupManager.getGroup(groupName) ?: return false

        if (user.getInheritedGroups(user.queryOptions).contains(group)) {
            user.data().remove(InheritanceNode.builder(group).build())
        }

        Gamemode4Core.luckPermsAPI.userManager.saveUser(user)

    } else {
        return false
    }

    return true
}
