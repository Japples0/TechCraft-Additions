ServerEvents.recipes(event => {
  const TCA = globalThis.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('shattered_heart'), [
    'EAE',
    'HTH',
    'EAE'
  ], {
    E: 'minecraft:echo_shard',
    A: 'minecraft:amethyst_shard',
    H: 'minecraft:heart_of_the_sea',
    T: TCA.item('illuminated_world_engine_tablet')
  }).id(TCA.developmentId('shattered_heart'))

  event.shapeless(TCA.item('splintered_echo'), [
    TCA.item('shattered_heart'),
    'minecraft:sculk_sensor',
    'minecraft:amethyst_shard'
  ]).id(TCA.developmentId('splintered_echo'))
})
