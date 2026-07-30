ServerEvents.recipes(event => {
  const TCA = {
    developmentRecipesEnabled: true,
    item: id => `techcraft_additions:${id}`,
    developmentId: id => `techcraft_additions:development/${id}`
  }
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
