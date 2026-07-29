ServerEvents.recipes(event => {
  const TCA = global.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('resonance_casing'), [
    'DSD',
    'SCS',
    'DED'
  ], {
    C: 'minecraft:deepslate',
    D: 'minecraft:polished_deepslate',
    E: 'minecraft:echo_shard',
    S: 'minecraft:sculk'
  }).id(TCA.developmentId('resonance_casing'))

  event.shaped(TCA.item('sculk_resonance_engine'), [
    'RCR',
    'FTF',
    'RCR'
  ], {
    R: 'deeperdarker:resonarium_plate',
    C: 'techcraft_additions:resonance_casing',
    F: TCA.item('finely_woven_time'),
    T: TCA.item('universe_tether')
  }).id(TCA.developmentId('sculk_resonance_engine'))
})
