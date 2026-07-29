ServerEvents.recipes(event => {
  const TCA = global.TechCraftAdditions
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
