package dev.moma.ecopicks.screen;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import dev.moma.ecopicks.screen.custom.LeafeeScreenData;
import dev.moma.ecopicks.screen.custom.LeafeeScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<LeafeeScreenHandler> LEAFEE_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(EcoPicks.MOD_ID, "leafee_screen_handler"),
            new ExtendedScreenHandlerType<>(
                    (syncId, inventory, data) -> {
                        LeafeeEntity leafee = (LeafeeEntity) inventory.player.getWorld()
                                .getEntityById(data.entityId());
                        return new LeafeeScreenHandler(syncId, inventory, new SimpleInventory(7), leafee);
                    },
                    LeafeeScreenData.PACKET_CODEC));

    public static void registerScreenHandlers() {
    }
}