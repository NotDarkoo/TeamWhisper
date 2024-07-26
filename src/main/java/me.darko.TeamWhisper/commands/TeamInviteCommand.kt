package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class TeamInviteCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teaminvite")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("[Team Whisper] Please specify a player to invite.")
            return false
        }

        val team = TeamManager.getTeamByPlayer(sender)
        if (team == null || !team.isOwner(sender)) {
            sender.sendMessage("[Team Whisper] You are not the owner of a team.")
            return true
        }

        val playerName = args[0]
        val invitedPlayer = Bukkit.getPlayer(playerName)
        if (invitedPlayer == null) {
            sender.sendMessage("[Team Whisper] Player not found.")
            return true
        }

        val limit = config.getInt("team-members-limit")
        val currentMembersCount = team.members.size

        if (currentMembersCount >= limit) {
            sender.sendMessage("[Team Whisper] Your team has reached the maximum member limit of $limit.")
            return true
        }

        if (invitedPlayer.uniqueId == sender.uniqueId) {
            sender.sendMessage("[Team Whisper] You cannot invite yourself.")
            return true
        }

        if (TeamManager.invitePlayer(team.name, invitedPlayer)) {
            sender.sendMessage("[Team Whisper] Player $playerName invited to the team.")
            invitedPlayer.sendMessage("[Team Whisper] You have been invited to join the team ${team.name}. Use /teamaccept to join.")
        } else {
            sender.sendMessage("[Team Whisper] Could not invite player $playerName.")
        }

        team.cleanExpiredInvites()
        return true
    }
}
