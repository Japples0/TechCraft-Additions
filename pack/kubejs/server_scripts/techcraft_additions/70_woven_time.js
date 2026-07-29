ServerEvents.recipes(event => {
  const TCA = globalThis.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shapeless(Item.of(TCA.item('loose_strands_of_time'), 4), [
    TCA.item('dimensional_descender'),
    'projecte:watch_of_flowing_time',
    'minecraft:string',
    'minecraft:sugar'
  ]).id(TCA.developmentId('loose_strands_of_time'))

  event.shaped(TCA.item('finely_woven_time'), [
    'SSS',
    'SCS',
    'SSS'
  ], {
    S: TCA.item('loose_strands_of_time'),
    C: 'minecraft:clock'
  }).id(TCA.developmentId('finely_woven_time'))
})
