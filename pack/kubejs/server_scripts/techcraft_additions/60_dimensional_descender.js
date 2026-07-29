ServerEvents.recipes(event => {
  const TCA = globalThis.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('universe_tether'), [
    'SLS',
    'ETE',
    'SLS'
  ], {
    S: 'minecraft:string',
    L: TCA.item('terrestrial_lattice'),
    E: 'minecraft:ender_eye',
    T: 'minecraft:lead'
  }).id(TCA.developmentId('universe_tether'))

  event.shaped(TCA.item('dimensional_descender'), [
    ' O ',
    'FTF',
    ' E '
  ], {
    O: TCA.item('otherworldly_fragment'),
    F: 'minecraft:feather',
    T: TCA.item('universe_tether'),
    E: 'minecraft:ender_eye'
  }).id(TCA.developmentId('dimensional_descender'))
})
