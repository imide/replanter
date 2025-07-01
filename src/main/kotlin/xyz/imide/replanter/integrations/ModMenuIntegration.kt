package xyz.imide.replanter.integrations

//? if fabric {

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi

class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory { parent ->
        ResourcefulConfigScreen.getFactory("Replanter").apply(parent)
    }
}

//?}