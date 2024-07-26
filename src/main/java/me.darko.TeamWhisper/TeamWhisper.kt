package me.darko.TeamWhisper
import me.darko.TeamWhisper.commands.*
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

import me.darko.TeamWhisper.listeners.*
import org.bukkit.entity.Player

class TeamWhisper : JavaPlugin() {
    private lateinit var config: FileConfiguration
    private lateinit var configFile: File
    private val playerChatMode = mutableMapOf<Player, Boolean>()
    override fun onEnable() {
        setupConfig()
        TeamManager.initialize(this)
        val teamChatCommand = TeamChatCommand(this.config, playerChatMode)
        registerCommands()
        registerListeners(teamChatCommand)
        logger.info("TeamWhisper enabled")
    }


    private fun registerListeners(teamChatCommand: TeamChatCommand) {
        Bukkit.getServer().pluginManager.registerEvents(TeamChatListener(teamChatCommand, playerChatMode), this)
        logger.info("Registered Listeners")
    }




    private fun setupConfig() {
        configFile = File(dataFolder, "config.yml")

        if (!configFile.exists()) {
            dataFolder.mkdirs()
            saveResource("config.yml", true)
        }

        config = YamlConfiguration.loadConfiguration(configFile)

        setDefaultConfigValues()
    }

    private fun setDefaultConfigValues() {
        val defaults = mapOf(
            "team-members-limit" to 10
        )

        defaults.forEach { (key, value) ->
            if (config.get(key) == null) {
                config.set(key, value)
            }
        }
        saveConfig()
    }

    private fun registerCommands() {
        getCommand("teamcreate")?.apply {
            setExecutor(TeamCreateCommand(config))
            permission = "teamwhisper.command.teamcreate"
        }

        getCommand("teaminvite")?.apply {
            setExecutor(TeamInviteCommand(config))
            permission = "teamwhisper.command.teaminvite"
        }

        getCommand("teamchat")?.apply {
            setExecutor(TeamChatCommand(config, playerChatMode))
            permission = "teamwhisper.command.teamchat"
        }

        getCommand("teamkick")?.apply {
            setExecutor(TeamKickCommand(config))
            permission = "teamwhisper.command.teamkick"
        }

        getCommand("teamdisband")?.apply {
            setExecutor(TeamDisbandCommand(config))
            permission = "teamwhisper.command.teamdisband"
        }

        getCommand("teamaccept")?.apply {
            setExecutor(TeamAcceptCommand(config))
            permission = "teamwhisper.command.teamaccept"
        }

        getCommand("teamleave")?.apply {
            setExecutor(TeamLeaveCommand(config))
            permission = "teamwhisper.command.teamleave"
        }

        getCommand("teaminfo")?.apply {
            setExecutor(TeamInfoCommand(config))
            permission = "teamwhisper.command.teaminfo"
        }

        getCommand("twreload")?.apply {
            setExecutor(TwReloadCommand(this@TeamWhisper))
            permission = "teamwhisper.command.reload"
        }

        getCommand("twhelp")?.apply {
            setExecutor(TwHelpCommand(config))
            permission = "teamwhisper.command.twhelp"
        }

        getCommand("teamwhisper")?.apply {
            setExecutor(TwHelpCommand(config))
            permission = "teamwhisper.command.twhelp"
        }

        logger.info("Registered commands with permissions and aliases")
    }

    override fun onDisable() {
        TeamManager.saveTeams()

        logger.info("TeamWhisper disabled")
    }

    override fun saveConfig() {
        try {
            (config as YamlConfiguration).save(configFile)
        } catch (e: Exception) {
            logger.severe("Could not save config file!")
            e.printStackTrace()
        }
    }
}
