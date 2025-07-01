package xyz.imide.replanter.config

import com.teamresourceful.resourcefulconfig.api.annotations.Comment
import com.teamresourceful.resourcefulconfig.api.annotations.Config
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo

@Config(
    value = "replanter",
)
@ConfigInfo(
    title = "Replanter",
    description = "Configuration for Replanter"
)
abstract class ReplanterConfig {
    companion object {
        @ConfigEntry(
            id = "enabled",
        )
        @JvmField
        var enabled: Boolean = true

        @ConfigEntry(
            id = "mustHoldHoe"
        )
        @Comment("If enabled, the player must hold a hoe in their hand in order to replant the crop automatically.")
        @JvmField
        var mustHoldHoe: Boolean = false
    }
}
