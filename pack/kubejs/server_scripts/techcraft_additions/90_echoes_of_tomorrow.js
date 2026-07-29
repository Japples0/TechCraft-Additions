ServerEvents.recipes(event => {
  const TCA = global.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shaped(TCA.item('echoes_of_tomorrow'), [
    'TDT',
    'FSF',
    'TDT'
  ], {
    T: TCA.item('finely_woven_time'),
    D: TCA.item('dimensional_descender'),
    F: TCA.item('otherworldly_fragment'),
    S: TCA.item('sculk_resonance_engine')
  }).id(TCA.developmentId('echoes_of_tomorrow'))
})
