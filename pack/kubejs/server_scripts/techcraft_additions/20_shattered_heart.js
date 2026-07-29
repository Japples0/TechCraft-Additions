ServerEvents.recipes(event => {
  const TCA = {
    developmentRecipesEnabled: true,
    item: id => `techcraft_additions:${id}`,
    developmentId: id => `techcraft_additions:development/${id}`
  }
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shapeless(TCA.item('splintered_echo'), [
    TCA.item('shattered_heart'),
    'minecraft:sculk_sensor',
    'minecraft:amethyst_shard'
  ]).id(TCA.developmentId('splintered_echo'))
})
