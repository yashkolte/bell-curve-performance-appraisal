// Utility functions for improved code organization

/**
 * Helper function for data processing
 * @param {any} data - Input data to process
 * @returns {any} Processed data
 */
export function processData(data) {
  if (!data) {
    return null;
  }
  
  return {
    ...data,
    processed: true,
    timestamp: new Date().toISOString()
  };
}

/**
 * Error handling utility
 * @param {Error} error - Error to handle
 * @param {string} context - Context information
 */
export function handleError(error, context = 'Unknown') {
  console.error(`Error in ${context}:`, error.message);
  
  // Add error tracking logic here
  return {
    error: true,
    message: error.message,
    context
  };
}