ServerEvents.recipes(event => {
  const TCA = globalThis.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('dark_world_engine_tablet'), [
    'SES',
    'EBE',
    'SAS'
  ], {
    S: 'minecraft:sculk',
    E: 'minecraft:echo_shard',
    B: 'minecraft:book',
    A: 'minecraft:amethyst_shard'
  }).id(TCA.developmentId('dark_world_engine_tablet'))

  event.shapeless(TCA.item('illuminated_world_engine_tablet'), [
    TCA.item('dark_world_engine_tablet'),
    'minecraft:glowstone_dust',
    'minecraft:amethyst_shard'
  ]).id(TCA.developmentId('illuminated_world_engine_tablet'))
})
