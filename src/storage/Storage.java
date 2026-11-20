package storage;

import java.io.IOException;
import java.util.List;

/**
 * Generic Storage Interface
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface: Defines a contract for data persistence without exposing file/database implementation details.
 * 2. Generics (<T>): Allows type-safe persistence across different model entities (User, Expense, Budget).
 * 3. Polymorphism: Services interact with the Storage interface, allowing different concrete storage
 *    implementations (CSV file storage, memory storage, binary storage) without changing business logic.
 *
 * @param <T> Model type managed by this storage
 */
public interface Storage<T> {

    /**
     * Persists all items in the given collection to the storage medium.
     *
     * @param items List of entities to persist
     * @throws IOException If an I/O error occurs during write
     */
    void saveAll(List<T> items) throws IOException;

    /**
     * Loads all persisted items from the storage medium.
     *
     * @return List of loaded entities
     * @throws IOException If an I/O error occurs during read
     */
    List<T> loadAll() throws IOException;
}
