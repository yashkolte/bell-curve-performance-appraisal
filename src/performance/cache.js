// Performance optimization utilities

class CacheManager {
  constructor() {
    this.cache = new Map();
    this.maxSize = 100;
  }
  
  /**
   * Get item from cache
   * @param {string} key - Cache key
   * @returns {any} Cached value or null
   */
  get(key) {
    const item = this.cache.get(key);
    if (item && item.expiry > Date.now()) {
      return item.value;
    }
    
    if (item) {
      this.cache.delete(key);
    }
    
    return null;
  }
  
  /**
   * Set item in cache
   * @param {string} key - Cache key
   * @param {any} value - Value to cache
   * @param {number} ttl - Time to live in milliseconds
   */
  set(key, value, ttl = 300000) {
    if (this.cache.size >= this.maxSize) {
      const firstKey = this.cache.keys().next().value;
      this.cache.delete(firstKey);
    }
    
    this.cache.set(key, {
      value,
      expiry: Date.now() + ttl
    });
  }
  
  /**
   * Clear cache
   */
  clear() {
    this.cache.clear();
  }
}

export default new CacheManager();