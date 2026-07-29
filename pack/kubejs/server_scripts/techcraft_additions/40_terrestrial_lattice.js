ServerEvents.recipes(event => {
  const TCA = {
    developmentRecipesEnabled: true,
    item: id => `techcraft_additions:${id}`,
    developmentId: id => `techcraft_additions:development/${id}`
  }
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('terrestrial_lattice'), [
    'OND',
    'GLG',
    'EWR'
  ], {
    O: TCA.item('overworld_attuned_rift'),
    N: TCA.item('nether_attuned_rift'),
    D: TCA.item('draconic_attuned_rift'),
    G: 'minecraft:gold_ingot',
    L: 'minecraft:lodestone',
    E: TCA.item('end_attuned_rift'),
    W: TCA.item('otherside_attuned_rift'),
    R: 'minecraft:redstone_block'
  }).id(TCA.developmentId('terrestrial_lattice'))
})
