package com.techcraft.additions.integration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.techcraft.additions.TechCraftAdditions;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class DimensionAtmospheres {
    private static final String RESOURCE_DIRECTORY = "dimensional_atmospheres";
    private static final Map<ResourceLocation, ResourceLocation> ATMOSPHERES = new HashMap<>();

    private static ResourceManager loadedResourceManager;

    private DimensionAtmospheres() {
    }

    public static Optional<Holder<Chemical>> getAtmosphere(Level level) {
        MinecraftServer server = level.getServer();
        if (server == null) {
            return Optional.empty();
        }

        ResourceManager resourceManager = server.getResourceManager();
        if (loadedResourceManager != resourceManager) {
            load(resourceManager);
        }

        ResourceLocation chemicalId = ATMOSPHERES.get(level.dimension().location());
        if (chemicalId == null) {
            return Optional.empty();
        }

        return level.registryAccess()
                .registry(MekanismAPI.CHEMICAL_REGISTRY_NAME)
                .flatMap(registry -> getChemical(registry, chemicalId));
    }

    private static Optional<Holder<Chemical>> getChemical(Registry<Chemical> registry, ResourceLocation chemicalId) {
        ResourceKey<Chemical> key = ResourceKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, chemicalId);
        return registry.getHolder(key).map(reference -> reference);
    }

    private static void load(ResourceManager resourceManager) {
        ATMOSPHERES.clear();
        loadedResourceManager = resourceManager;

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                RESOURCE_DIRECTORY,
                resourceLocation -> resourceLocation.getPath().endsWith(".json")
        );

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            loadResource(entry.getKey(), entry.getValue());
        }
    }

    private static void loadResource(ResourceLocation resourceId, Resource resource) {
        try (BufferedReader reader = resource.openAsReader()) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            ResourceLocation dimension = readId(json, "dimension", resourceId);
            ResourceLocation chemical = readId(json, "chemical", resourceId);
            ATMOSPHERES.put(dimension, chemical);
        } catch (IOException | RuntimeException exception) {
            TechCraftAdditions.LOGGER.warn("Failed to load dimension atmosphere '{}'", resourceId, exception);
        }
    }

    private static ResourceLocation readId(JsonObject json, String key, ResourceLocation resourceId) {
        JsonElement element = json.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            throw new IllegalStateException("Missing string field '" + key + "' in " + resourceId);
        }
        return ResourceLocation.parse(element.getAsString());
    }
}
