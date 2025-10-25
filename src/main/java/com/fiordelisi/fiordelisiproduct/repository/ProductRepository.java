package com.fiordelisi.fiordelisiproduct.repository;

import com.fiordelisi.fiordelisiproduct.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByCategoryId(String categoryId);
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    /**
     * <b>Finds products by category ID and product name with language fallback logic.</b>
     *
     * <p><b>Parameter mapping:</b></p>
     * <ul>
     *   <li><b>?0 → categoryId:</b> the product's category ID (e.g., <code>"64f1afa..."</code>)</li>
     *   <li><b>?1 → value:</b> the full or partial product name to search for (e.g., <code>"milk"</code>, <code>"coffee"</code>)</li>
     *   <li><b>?2 → languageCode:</b> the target language code (<code>"vi"</code>, <code>"en"</code>, etc.)</li>
     * </ul>
     *
     * <p><b>Example document structure:</b></p>
     * <pre>
     * {
     *   "categoryId": ["64f1afa...",....],
     *   "name": [
     *     { "language": "vi", "value": "Cà phê sữa" },
     *     { "language": "en", "value": "Milk Coffee" }
     *   ]
     * }
     * </pre>
     *
     * <p><b>Query logic:</b></p>
     * <ul>
     *   <li>If the product has a translation in the specified <code>languageCode</code>, search by that language.</li>
     *   <li>If no translation exists for that <code>languageCode</code>, fallback to Vietnamese (<code>"vi"</code>).</li>
     *   <li>The search on the name field uses a case-insensitive regular expression (<code>$options: 'i'</code>).</li>
     * </ul>
     *
     * <p><b>Example behavior:</b></p>
     * <pre>
     * languageCode = "en", value = "milk"
     * → Finds products where name.language = "en" and name.value contains "milk".
     * → If no English translation exists, searches name.language = "vi" for "milk".
     * </pre>
     */
    @Query("""
        {
          'categoryId': { '$in': [ ?0 ] },
          '$or': [
            { 'name': { '$elemMatch': { 'language': ?2, 'value': { '$regex': ?1, '$options': 'i' } } } },
            {
              '$and': [
                { 'name': { '$not': { '$elemMatch': { 'language': ?2 } } } },
                { 'name': { '$elemMatch': { 'language': 'vi', 'value': { '$regex': ?1, '$options': 'i' } } } }
              ]
            }
          ]
        }
    """)
    Page<Product> findByCategoryIdAndNameContainingIgnoreCaseAndLanguageWithFallback(
            String categoryId, String value, String languageCode, Pageable pageable);



    /**
     * <b>Finds products by name only (without category filtering) with language fallback logic.</b>
     *
     * <p><b>Parameter mapping:</b></p>
     * <ul>
     *   <li><b>?0 → value:</b> the full or partial product name to search for (e.g., <code>"milk"</code>, <code>"coffee"</code>)</li>
     *   <li><b>?1 → languageCode:</b> the target language code (<code>"vi"</code>, <code>"en"</code>, etc.)</li>
     * </ul>
     *
     * <p><b>Example document structure:</b></p>
     * <pre>
     * {
     *   "name": [
     *     { "language": "vi", "value": "Trà xanh" },
     *     { "language": "en", "value": "Green Tea" }
     *   ]
     * }
     * </pre>
     *
     * <p><b>Query logic:</b></p>
     * <ul>
     *   <li>If the product has a translation in the specified <code>languageCode</code>, search by that language.</li>
     *   <li>If no translation exists for that <code>languageCode</code>, fallback to Vietnamese (<code>"vi"</code>).</li>
     *   <li>The search on the name field uses a case-insensitive regular expression (<code>$options: 'i'</code>).</li>
     * </ul>
     *
     * <p><b>Example behavior:</b></p>
     * <pre>
     * languageCode = "en", value = "tea"
     * → Finds products where name.language = "en" and name.value contains "tea".
     * → If no English translation exists, searches name.language = "vi" for "tea".
     * </pre>
     */

    @Query("""
    {
      '$or': [
        { 'name': { '$elemMatch': { 'language': ?1, 'value': { '$regex': ?0, '$options': 'i' } } } },
        {
          '$and': [
            { 'name': { '$not': { '$elemMatch': { 'language': ?1 } } } },
            { 'name': { '$elemMatch': { 'language': 'vi', 'value': { '$regex': ?0, '$options': 'i' } } } }
          ]
        }
      ]
    }
    """)
    Page<Product> findByNameContainingIgnoreCaseAndLanguageWithFallback(
            String value, String languageCode, Pageable pageable);

}

