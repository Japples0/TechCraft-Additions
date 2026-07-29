ServerEvents.recipes(event => {
  const TCA = {
    developmentRecipesEnabled: true,
    item: id => `techcraft_additions:${id}`,
    developmentId: id => `techcraft_additions:development/${id}`
  }
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('dimensional_rift'), [
    'PEP',
    'HSH',
    'PAP'
  ], {
    P: 'minecraft:ender_pearl',
    E: 'minecraft:echo_shard',
    H: TCA.item('shattered_heart'),
    S: TCA.item('splintered_echo'),
    A: 'minecraft:amethyst_shard'
  }).id(TCA.developmentId('dimensional_rift'))

  event.shapeless(TCA.item('overworld_attuned_rift'), [
    TCA.item('dimensional_rift'),
    'minecraft:grass_block',
    'minecraft:emerald'
  ]).id(TCA.developmentId('overworld_attuned_rift'))

  event.shapeless(TCA.item('draconic_attuned_rift'), [
    TCA.item('dimensional_rift'),
    'draconicevolution:draconium_dust',
    'minecraft:dragon_breath'
  ]).id(TCA.developmentId('draconic_attuned_rift'))
})
