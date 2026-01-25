package dev.moma.ecopicks.entity;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
  public static final EntityType<LeafeeEntity> LEAFEE = Registry.register(Registries.ENTITY_TYPE,
      Identifier.of(EcoPicks.MOD_ID, "leafee"),
      EntityType.Builder.create(LeafeeEntity::new, SpawnGroup.CREATURE)
          .dimensions(0.8F, 1.2F).build());

  public static void registerModEntities() {
    EcoPicks.LOGGER.info("Registering Entities for " + EcoPicks.MOD_ID);
  }
}
