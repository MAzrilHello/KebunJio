export function sanitizeInput(input) {
    if (typeof input !== 'string') {
      return input; // If not a string, return as is
    }
  
    return input
      .replace(/<script.*?>.*?<\/script>/gi, '') // Remove script tags
      .replace(/[<>]/g, ''); // Remove HTML brackets
  }
  
  export default { sanitizeInput };