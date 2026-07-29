ServerEvents.recipes(event => {
  const TCA = global.TechCraftAdditions
  if (!TCA || !TCA.developmentRecipesEnabled) return

  event.shapeless(TCA.item('splintered_echo'), [
    TCA.item('shattered_heart'),
    'minecraft:sculk_sensor',
    'minecraft:amethyst_shard'
  ]).id(TCA.developmentId('splintered_echo'))
})
