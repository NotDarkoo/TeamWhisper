package me.darko.TeamWhisper

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.configuration.file.FileConfiguration
import java.util.*

data class Team(val name: String, val owner: OfflinePlayer, private val config: FileConfiguration ) {
    val ownerUUID: UUID = owner.uniqueId
    val members = mutableSetOf<UUID>()
    private val invitedPlayers = mutableMapOf<UUID, Long>()

    init {
        members.add(ownerUUID)
    }

    fun isOwner(player: Player): Boolean {
        return ownerUUID == player.uniqueId
    }

    fun isMember(player: Player): Boolean {
        return members.contains(player.uniqueId)
    }

    fun isInvited(player: Player): Boolean {
        val inviteTime = invitedPlayers[player.uniqueId] ?: return false
        return System.currentTimeMillis() - inviteTime <= 60000 // 60 seconds
    }

    fun invite(player: Player) {
        invitedPlayers[player.uniqueId] = System.currentTimeMillis()
    }

    private fun getMaxMembers(): Int {
        return config.getInt("team-members-limit", 10)
    }

    fun addMember(player: Player): Boolean {
        if (members.size >= getMaxMembers()) {
            return false
        }
        invitedPlayers.remove(player.uniqueId)
        members.add(player.uniqueId)
        return true
    }

    fun removeMember(player: Player) {
        members.remove(player.uniqueId)
    }

    fun sendMessage(message: String) {
        val prefix = "${ChatColor.BLUE}[TEAM] ${ChatColor.RESET}"
        val fullMessage = "$prefix$message"
        members.mapNotNull { Bukkit.getPlayer(it) }.forEach { it.sendMessage(fullMessage) }
    }

    fun getMem(): Set<UUID> = members
    fun cleanExpiredInvites() {
        val currentTime = System.currentTimeMillis()
        invitedPlayers.entries.removeIf { (uuid, timestamp) ->
            currentTime - timestamp > 60000 // 60 seconds
        }
    }

}
