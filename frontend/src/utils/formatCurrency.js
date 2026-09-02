/**
 * Format a numeric amount as Indian Rupees (₹).
 *
 * Examples:
 *   formatCurrency(499)    → "₹499"
 *   formatCurrency(1299)   → "₹1,299"
 *   formatCurrency(12500)  → "₹12,500"
 *   formatCurrency(null)   → "₹0"
 */

const inrFormatter = new Intl.NumberFormat('en-IN', {
  style: 'currency',
  currency: 'INR',
  minimumFractionDigits: 0,
  maximumFractionDigits: 2,
});

export const formatCurrency = (amount) => {
  if (amount === null || amount === undefined || isNaN(amount)) {
    return '₹0';
  }
  return inrFormatter.format(amount);
};

export default formatCurrency;
