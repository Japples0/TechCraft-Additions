ServerEvents.recipes(event => {
  const TCA = {
    developmentRecipesEnabled: true,
    item: id => `techcraft_additions:${id}`,
    developmentId: id => `techcraft_additions:development/${id}`
  }
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('otherworldly_fragment'), [
    'RER',
    'GLG',
    'RER'
  ], {
    R: 'deeperdarker:reinforced_echo_shard',
    E: 'minecraft:echo_shard',
    G: 'minecraft:glowstone_dust',
    L: TCA.item('terrestrial_lattice')
  }).id(TCA.developmentId('otherworldly_fragment'))
})
