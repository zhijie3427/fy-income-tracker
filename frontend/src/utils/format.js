export function roundMoney(value) {
  return Math.round(Number(value) || 0)
}

export function formatTwoWan(value) {
  const amount = roundMoney(value)
  if (amount >= 10000) {
    const wan = Math.floor(amount / 10000)
    const rest = amount % 10000
    return rest ? `${wan}万 ${rest}两` : `${wan}万两`
  }
  return `${amount}两`
}
