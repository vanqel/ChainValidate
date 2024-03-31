import errors.ValidationError

class ChainValidate<T> {
    /**
     * Лист хранящий в себе валидаторы
     */
    private var chains: List<Pair<ValidatorInterface<T>, T?>> = mutableListOf()

    /**
     * Создание валдитора с пустым списком, что бы не было коллизий
     */
    fun builder(): ChainValidate<T> {
        chains = mutableListOf()
        return this
    }

    /**
     * Добавление нового валидатора в конец списка
     */
    fun addChain(
        validator: ValidatorInterface<T>,
        value: T?,
    ): ChainValidate<T> {
        chains.addLast(validator to value)
        return this
    }

    /**
     * Возвращает первую попавшуюся ошибку валидации, если её нету, возвращает null
     */
    fun findFirstException(): Throwable? {
        chains.asSequence()
            .mapNotNull { it.first.valid(it.second) }
            .firstOrNull()
            ?.let {
                throw it
            } ?: return null
    }

    /**
     * Возвращает все ошибки валидации, если её нету, возвращает null
     */
    fun findAllException(): Throwable? {
        val err = mutableMapOf<String, String>()
        var header: String? = null
        chains.asSequence()
            .forEach { r ->
                r.first.valid(r.second)
                    ?.let {
                        header = it.message
                        err.putAll(it.errors)
                    }
            }
        return if (err.isNotEmpty() && header != null) {
            throw ValidationError(header!!, err)
        } else {
            null
        }
    }
}
